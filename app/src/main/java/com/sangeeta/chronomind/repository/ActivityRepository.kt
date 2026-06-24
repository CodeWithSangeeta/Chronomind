//package com.sangeeta.chronomind.repository
//
//import com.sangeeta.chronomind.local.db.dao.ActivityDao
//import com.sangeeta.chronomind.local.db.dao.SessionDao
//import com.sangeeta.chronomind.local.db.entity.ActivityEntity
//import com.sangeeta.chronomind.local.db.entity.SessionEntity
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import java.time.LocalDate
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//@Singleton
//class ActivityRepository @Inject constructor(
//    private val dao: ActivityDao,
//    private val sessionDao: SessionDao
//) {
//    private val _selectedActivityId = MutableStateFlow<Int?>(null)
//    val selectedActivityId: StateFlow<Int?> = _selectedActivityId.asStateFlow()
//
//    fun selectActivity(activityId: Int) {
//        _selectedActivityId.value = activityId
//    }
//
//    fun clearSelectedActivity() {
//        _selectedActivityId.value = null
//    }
//
//    fun observeAll(): Flow<List<ActivityEntity>> = dao.observeAll()
//
//    fun observeById(id: Int): Flow<ActivityEntity?> = dao.observeById(id)
//
//    fun observeRunning(): Flow<ActivityEntity?> = dao.observeRunning()
//
//    suspend fun add(activity: ActivityEntity): Long = dao.insert(activity)
//
//    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)
//
//    suspend fun update(activity: ActivityEntity) = dao.update(activity)
//
//    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) =
//        dao.updateTimer(id, elapsed, running)
//
//    suspend fun updateStreak(id: Int, streak: Int, date: String) =
//        dao.updateStreak(id, streak, date)
//
//    suspend fun stopAll() = dao.stopAll()
//
//    suspend fun delete(activity: ActivityEntity) = dao.delete(activity)
//
//    suspend fun clearAll() = dao.deleteAll()
//
//    suspend fun startActivity(activity: ActivityEntity) {
//        dao.stopAll()
//        dao.updateTimer(
//            id = activity.id,
//            elapsed = activity.elapsedSeconds,
//            running = true
//        )
//        selectActivity(activity.id)
//    }
//
//    suspend fun pauseActivity(activity: ActivityEntity) {
//        dao.updateTimer(
//            id = activity.id,
//            elapsed = activity.elapsedSeconds,
//            running = false
//        )
//        selectActivity(activity.id)
//    }
//
//    suspend fun finishActivity(activity: ActivityEntity, todayLabel: String) {
//        dao.updateTimer(
//            id = activity.id,
//            elapsed = 0L,
//            running = false
//        )
//        dao.updateStreak(
//            id = activity.id,
//            streak = activity.streakDays + 1,
//            date = todayLabel
//        )
//        selectActivity(activity.id)
//    }
//
//    suspend fun switchToActivity(next: ActivityEntity) {
//        dao.stopAll()
//        dao.updateTimer(
//            id = next.id,
//            elapsed = next.elapsedSeconds,
//            running = true
//        )
//        selectActivity(next.id)
//    }
//
//    suspend fun logSession(activity: ActivityUiModel, isCompleted: Boolean) {
//        sessionDao.insert(
//            SessionEntity(
//                activityId = activity.id,
//                activityName = activity.name,
//                activityIcon = activity.icon,
//                activityColorHex = activity.colorHex,
//                elapsedSeconds = activity.elapsedSeconds,
//                targetSeconds = activity.targetSeconds,
//                isCompleted = isCompleted,
//                dateLabel = LocalDate.now().toString()
//            )
//        )
//    }
//
//    fun observeSessionsSince(from: String): Flow<List<SessionEntity>> =
//        sessionDao.observeSince(from)
//}
//



package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.dao.SessionDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import java.time.LocalDate
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
     * FIX: Streak logic is now correct.
     *
     * Rules:
     * 1. Only increment streak if today != lastActiveDate (first completion today).
     * 2. continueStreakOnMiss = true  → streak always increments on completion, never resets passively.
     * 3. continueStreakOnMiss = false → handled by [resetMissedStreaks] called at midnight.
     *    On completion, we just set lastActiveDate = today so midnight check knows user was active.
     *
     * We do NOT double-increment if user completes the same activity twice today.
     */
    suspend fun finishActivity(activity: ActivityEntity) {
        val today = LocalDate.now().toString()
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
     * - lastActiveDate is not today (user missed yesterday or before)
     */
    suspend fun resetMissedStreaks() {
        val today = LocalDate.now().toString()
        dao.resetMissedStreaks(today)
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
                dateLabel = LocalDate.now().toString()
            )
        )
    }

    fun observeSessionsSince(from: String): Flow<List<SessionEntity>> =
        sessionDao.observeSince(from)
}