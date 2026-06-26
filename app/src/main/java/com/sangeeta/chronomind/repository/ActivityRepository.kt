package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.dao.SessionDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

@Singleton
class ActivityRepository @Inject constructor(
    private val dao: ActivityDao,
    private val sessionDao: SessionDao
) {

    // ── Selected activity for Home screen ──────────────────────────────────

    private val _selectedActivityId = MutableStateFlow<Int?>(null)
    val selectedActivityId: StateFlow<Int?> = _selectedActivityId.asStateFlow()

    fun selectActivity(activityId: Int) { _selectedActivityId.value = activityId }
    fun clearSelectedActivity() { _selectedActivityId.value = null }

    // ── Observe ────────────────────────────────────────────────────────────

    fun observeAll(): Flow<List<ActivityEntity>> = dao.observeAll()
    fun observeById(id: Int): Flow<ActivityEntity?> = dao.observeById(id)
    fun observeRunning(): Flow<ActivityEntity?> = dao.observeRunning()

    // ── CRUD ───────────────────────────────────────────────────────────────

    suspend fun add(activity: ActivityEntity): Long = dao.insert(activity)
    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)
    suspend fun update(activity: ActivityEntity) = dao.update(activity)
    suspend fun delete(activity: ActivityEntity) = dao.delete(activity)
    suspend fun clearAll() = dao.deleteAll()
    suspend fun stopAll() = dao.stopAll()

    // Fine-grained tick update — called every second by the service.
    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) =
        dao.updateTimer(id, elapsed, running)

    // ── Session state machine ──────────────────────────────────────────────
    //
    //  States an activity can be in:
    //
    //  IDLE          elapsedSeconds=0, isRunning=false, hasPendingSession=false
    //  RUNNING       elapsedSeconds>0, isRunning=true,  hasPendingSession=false
    //  PENDING       elapsedSeconds>0, isRunning=false, hasPendingSession=true
    //  COMPLETED     → written to sessions table, then reset to IDLE
    //  ABANDONED     → written to sessions table as incomplete, then reset to IDLE
    //
    //  Transitions:
    //    startFresh / resumeOrStart → RUNNING
    //    pauseSession               → PENDING
    //    completeSession            → COMPLETED (then IDLE)
    //    abandonToHistory           → ABANDONED (then IDLE)
    //    switchToActivity           → pauses current (→ PENDING), starts next (→ RUNNING)

    /**
     * Start an activity from scratch (elapsed = 0) or resume an existing
     * pending session from today.
     *
     * Rules:
     * - If [activity].hasPendingSession AND pendingSessionDate == today → resume (keep elapsed).
     * - Otherwise → start fresh (reset elapsed to 0).
     *
     * In both cases, stop any currently running activity first.
     * The currently running activity is just paused in-memory (isRunning=false),
     * NOT pushed to history — that is the caller's responsibility via switchToActivity().
     */
    suspend fun resumeOrStart(activity: ActivityEntity) {
        val today = getTodayDateString()
        dao.stopAll()

        val isSameDayResume = activity.hasPendingSession &&
                activity.pendingSessionDate == today &&
                activity.elapsedSeconds > 0L

        val elapsed = if (isSameDayResume) activity.elapsedSeconds else 0L

        dao.updateTimer(activity.id, elapsed, running = true)

        // If we reset elapsed, also clear the stale pending flag.
        if (!isSameDayResume) {
            dao.updatePendingFlag(activity.id, hasPending = false, pendingDate = "")
        }

        selectActivity(activity.id)
    }

    /**
     * Pause the current session.
     * - Sets isRunning = false.
     * - Sets hasPendingSession = true + records today's date.
     * - Does NOT write to history.
     *
     * The pending record remains open until the user either
     * completes the session or the day ends (handled by StreakResetWorker).
     */
    suspend fun pauseSession(activity: ActivityEntity) {
        val today = getTodayDateString()
        dao.updateTimer(activity.id, activity.elapsedSeconds, running = false)
        dao.updatePendingFlag(activity.id, hasPending = true, pendingDate = today)
        selectActivity(activity.id)
    }

    /**
     * Switch from the currently running/pending activity to [next].
     *
     * - If an activity is currently RUNNING, pause it (→ PENDING).
     * - If [next] is the same as the running one, just resume it (no-op switch).
     * - Starts [next] via resumeOrStart().
     */
    suspend fun switchToActivity(next: ActivityEntity) {
        val currentRunning = dao.observeRunning().firstOrNull()

        if (currentRunning != null && currentRunning.id != next.id) {
            // Pause the running activity → keeps it as PENDING (not logged yet).
            pauseSession(currentRunning)
        }

        resumeOrStart(next)
    }

    /**
     * Mark a session as COMPLETED.
     *
     * Called when:
     *   - User taps "Finish" manually.
     *   - Timer auto-ends (TIMER_END completion style).
     *
     * Steps:
     *  1. Log to sessions table as completed.
     *  2. Update streak.
     *  3. Reset activity to IDLE (elapsed=0, isRunning=false, pending cleared).
     */
    suspend fun completeSession(activity: ActivityEntity, finalElapsed: Long) {
        val today = getTodayDateString()

        // 1. Write to history as completed.
        sessionDao.insert(
            SessionEntity(
                activityId = activity.id,
                activityName = activity.name,
                activityIcon = activity.icon,
                activityColorHex = activity.colorHex,
                elapsedSeconds = finalElapsed,
                targetSeconds = activity.targetMinutes * 60L,
                isCompleted = true,
                dateLabel = today
            )
        )

        // 2. Update streak — only increment on first completion of today.
        val isFirstCompletionToday = activity.lastActiveDate != today
        val newStreak = if (isFirstCompletionToday) activity.streakDays + 1 else activity.streakDays
        dao.updateStreak(activity.id, newStreak, today)

        // 3. Reset to IDLE.
        dao.resetSession(activity.id)

        selectActivity(activity.id)
    }

    /**
     * Abandon a pending session at end-of-day (or when explicitly discarded).
     *
     * Called by:
     *  - StreakResetWorker at midnight for stale pending sessions.
     *
     * Only writes to history if meaningful elapsed time exists (> 10 seconds)
     * to avoid cluttering history with accidental taps.
     *
     * The session is written as isCompleted = false with partial elapsed time.
     * The UI will show an incomplete progress bar based on elapsedSeconds / targetSeconds.
     */
    suspend fun abandonToHistory(activity: ActivityEntity) {
        if (activity.elapsedSeconds > 10L) {
            sessionDao.insert(
                SessionEntity(
                    activityId = activity.id,
                    activityName = activity.name,
                    activityIcon = activity.icon,
                    activityColorHex = activity.colorHex,
                    elapsedSeconds = activity.elapsedSeconds,
                    targetSeconds = activity.targetMinutes * 60L,
                    isCompleted = false,
                    dateLabel = activity.pendingSessionDate.ifEmpty { getTodayDateString() }
                )
            )
        }

        // Reset to IDLE regardless of whether we wrote to history.
        dao.resetSession(activity.id)
    }

    /**
     * Called by StreakResetWorker at midnight.
     *
     * Two jobs:
     *  1. Finalize all stale pending sessions (from yesterday or earlier) into history.
     *  2. Reset streaks for activities the user missed.
     */
    suspend fun runMidnightMaintenance() {
        val today = getTodayDateString()
        val yesterday = getYesterdayDateString()

        // Step 1: Abandon any pending sessions that were NOT completed before midnight.
        val stalePending = dao.getStalePendingSessions(today)
        stalePending.forEach { abandonToHistory(it) }

        // Step 2: Reset streaks for activities missed yesterday.
        dao.resetMissedStreaks(yesterday)
    }

    // ── Session observation ────────────────────────────────────────────────

    fun observeSessionsSince(from: String): Flow<List<SessionEntity>> =
        sessionDao.observeSince(from)

    // ── Appearance update (icon / color) ──────────────────────────────────

    suspend fun updateAppearance(id: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, colorHex: String) =
        dao.updateAppearance(id, icon, colorHex)

    // ── Date helpers (API 24 compatible) ──────────────────────────────────

    fun getTodayDateString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }
}