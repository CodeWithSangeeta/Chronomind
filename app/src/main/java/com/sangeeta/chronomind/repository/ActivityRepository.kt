package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.dao.SessionDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.local.db.entity.SessionEntity
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
    suspend fun updateElapsedSnapshot(id: Int, elapsed: Long, running: Boolean) =
        dao.updateElapsedSnapshot(id, elapsed, running)


    suspend fun resumeOrStart(activity: ActivityEntity) {
        val today = getTodayDateString()
        if (activity.completedDate == today) return
        val now = nowMillis()

        dao.stopAll()

        val isSameDayResume = activity.hasPendingSession &&
                activity.pendingSessionDate == today &&
                activity.elapsedSeconds > 0L

        val accumulated = if (isSameDayResume) {
            activity.elapsedSeconds
        } else {
            0L
        }
        val endsAt = computeEndsAt(activity, now, accumulated)

//        val endsAt = if (activity.targetType != "STOPWATCH") {
//            val targetSeconds = activity.targetMinutes * 60L
//            now + ((targetSeconds - accumulated).coerceAtLeast(0L) * 1000L)
//        } else {
//            null
//        }

        dao.startSession(
            id = activity.id,
            startedAt = now,
            endsAt = endsAt,
            accumulatedElapsed = accumulated
        )
        if (!isSameDayResume) {
            dao.updateElapsedSnapshot(activity.id, 0L, true)
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
        val elapsed = computeElapsedSeconds(activity)

        dao.pauseSession(
            id = activity.id,
            elapsedSeconds = elapsed,
            pendingDate = today
        )

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
    suspend fun completeSession(activity: ActivityEntity, finalElapsed: Long? = null) {
        val today = getTodayDateString()
        val actualElapsed = finalElapsed ?: computeElapsedSeconds(activity)
        // 1. Write to history as completed.
        sessionDao.insert(
            SessionEntity(
                activityId = activity.id,
                activityName = activity.name,
                activityIcon = activity.icon,
                activityColorHex = activity.colorHex,
                elapsedSeconds = actualElapsed,
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
        dao.markCompletedDate(activity.id, today)

        selectActivity(activity.id)
    }


    suspend fun abandonToHistory(activity: ActivityEntity) {
        val actualElapsed = if (activity.isRunning) computeElapsedSeconds(activity) else activity.elapsedSeconds

        if (actualElapsed > 10L) {
            sessionDao.insert(
                SessionEntity(
                    activityId = activity.id,
                    activityName = activity.name,
                    activityIcon = activity.icon,
                    activityColorHex = activity.colorHex,
                    elapsedSeconds = actualElapsed,
                    targetSeconds = activity.targetMinutes * 60L,
                    isCompleted = false,
                    dateLabel = activity.pendingSessionDate.ifEmpty { getTodayDateString() }
                )
            )
        }

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

    private fun nowMillis(): Long = System.currentTimeMillis()

    fun computeElapsedSeconds(activity: ActivityEntity, now: Long = nowMillis()): Long {
        if (!activity.isRunning || activity.sessionStartedAtEpochMillis == null) {
            return activity.accumulatedElapsedBeforeStartSeconds
        }
        val delta = ((now - activity.sessionStartedAtEpochMillis) / 1000L).coerceAtLeast(0L)
        return activity.accumulatedElapsedBeforeStartSeconds + delta
    }

    fun computeRemainingSeconds(activity: ActivityEntity, now: Long = nowMillis()): Long {
        val endsAt = activity.sessionEndsAtEpochMillis ?: return 0L
        return ((endsAt - now) / 1000L).coerceAtLeast(0L)
    }

    private fun computeEndsAt(activity: ActivityEntity, now: Long, accumulated: Long): Long? {
        return if (activity.targetType == "STOPWATCH") {
            null
        } else {
            val targetSeconds = activity.targetMinutes * 60L
            now + ((targetSeconds - accumulated).coerceAtLeast(0L) * 1000L)
        }
    }


    fun buildDisplayState(activity: ActivityEntity, now: Long = System.currentTimeMillis()): ActivityDisplayState {
        val today = getTodayDateString()
        val isCompletedToday = activity.completedDate == today

        val elapsed = computeElapsedSeconds(activity, now)
        val remaining = computeRemainingSeconds(activity, now)

        val sessionState = when {
            isCompletedToday -> ActivitySessionState.COMPLETED_TODAY
            activity.isRunning -> ActivitySessionState.RUNNING
            activity.hasPendingSession && activity.pendingSessionDate == today -> ActivitySessionState.PENDING
            else -> ActivitySessionState.IDLE
        }

        val displayTime = when {
            isCompletedToday -> formatTime(elapsed)
            activity.targetType == "STOPWATCH" -> formatTime(elapsed)
            activity.isRunning -> formatTime(remaining)
            sessionState == ActivitySessionState.PENDING -> formatTime(remaining)
            else -> formatTime(activity.targetMinutes * 60L)
        }

        val progress = if (activity.targetType == "STOPWATCH" || activity.targetMinutes == 0) 0f
        else (elapsed.toFloat() / (activity.targetMinutes * 60f)).coerceIn(0f, 1f)

        return ActivityDisplayState(
            activityId = activity.id,
            name = activity.name,
            icon = activity.icon,
            colorHex = activity.colorHex,
            sessionState = sessionState,
            isStopwatch = activity.targetType == "STOPWATCH",
            displayTime = displayTime,
            progress = progress,
            streakDays = activity.streakDays,
            targetSeconds = activity.targetMinutes * 60L,
            elapsedSeconds = elapsed,
            remainingSeconds = if (activity.targetType == "STOPWATCH") 0L else remaining,
            canStart = !isCompletedToday,
            isRunning = activity.isRunning
        )
    }

    private fun formatTime(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) "%02d:%02d:%02d".format(h, m, s)
        else "%02d:%02d".format(m, s)
    }

    suspend fun syncDayBoundaryIfNeeded() {
        val today = getTodayDateString()
        val stalePending = dao.getStalePendingSessions(today)
        stalePending.forEach { abandonToHistory(it) }
    }
}