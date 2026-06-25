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

@Singleton
class ActivityRepository @Inject constructor(
    private val dao: ActivityDao,
    private val sessionDao: SessionDao
) {

    // --- Selected activity for Home screen ---
    private val _selectedActivityId = MutableStateFlow<Int?>(null)
    val selectedActivityId: StateFlow<Int?> = _selectedActivityId.asStateFlow()

    fun selectActivity(activityId: Int) { _selectedActivityId.value = activityId }
    fun clearSelectedActivity() { _selectedActivityId.value = null }

    // --- Observe ---
    fun observeAll(): Flow<List<ActivityEntity>> = dao.observeAll()
    fun observeById(id: Int): Flow<ActivityEntity?> = dao.observeById(id)
    fun observeRunning(): Flow<ActivityEntity?> = dao.observeRunning()

    // --- CRUD ---
    suspend fun add(activity: ActivityEntity): Long = dao.insert(activity)
    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)
    suspend fun update(activity: ActivityEntity) = dao.update(activity)
    suspend fun delete(activity: ActivityEntity) = dao.delete(activity)
    suspend fun clearAll() = dao.deleteAll()
    suspend fun stopAll() = dao.stopAll()
    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) =
        dao.updateTimer(id, elapsed, running)
    suspend fun updateStreak(id: Int, streak: Int, date: String) =
        dao.updateStreak(id, streak, date)

    // --- Timer actions ---
    suspend fun startActivity(activity: ActivityEntity) {
        dao.stopAll()
        dao.updateTimer(id = activity.id, elapsed = activity.elapsedSeconds, running = true)
        selectActivity(activity.id)
    }

    suspend fun pauseActivity(activity: ActivityEntity) {
        dao.updateTimer(id = activity.id, elapsed = activity.elapsedSeconds, running = false)
        selectActivity(activity.id)
    }

    suspend fun switchToActivity(next: ActivityEntity) {
        dao.stopAll()
        dao.updateTimer(id = next.id, elapsed = next.elapsedSeconds, running = true)
        selectActivity(next.id)
    }

    /**
     * Streak logic:
     * 1. Only increment streak if today != lastActiveDate (first completion today).
     * 2. continueStreakOnMiss = true  → streak always increments on completion.
     * 3. continueStreakOnMiss = false → resets if day was missed (checked at midnight).
     */
    suspend fun finishActivity(activity: ActivityEntity) {
        val today = getTodayDateString()
        val isFirstCompletionToday = activity.lastActiveDate != today
        val newStreak = if (isFirstCompletionToday) activity.streakDays + 1 else activity.streakDays

        dao.updateTimer(id = activity.id, elapsed = 0L, running = false)
        dao.updateStreak(id = activity.id, streak = newStreak, date = today)
        selectActivity(activity.id)
    }

    /**
     * Called by StreakResetWorker at midnight.
     * Resets streak to 0 for activities where:
     * - continueStreakOnMiss = false (user chose "Reset streak on miss")
     * - lastActiveDate is older than yesterday (meaning user missed the entire previous day)
     */
    suspend fun resetMissedStreaks() {
        val yesterday = getYesterdayDateString()
        dao.resetMissedStreaks(yesterday)
    }

    // --- Session logging ---
    suspend fun logSession(activity: ActivityUiModel, isCompleted: Boolean) {
        sessionDao.insert(
            SessionEntity(
                activityId = activity.id,
                activityName = activity.name,
                activityIcon = activity.icon,
                activityColorHex = activity.colorHex,
                elapsedSeconds = activity.elapsedSeconds,
                targetSeconds = activity.targetSeconds,
                isCompleted = isCompleted,
                dateLabel = getTodayDateString()
            )
        )
    }

    fun observeSessionsSince(from: String): Flow<List<SessionEntity>> =
        sessionDao.observeSince(from)

    // --- Date Helpers (Compatible with API 24+) ---
    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }
}
