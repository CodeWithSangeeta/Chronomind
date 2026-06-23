package com.sangeeta.chronomind.repository


//@Singleton
//class ActivityRepository @Inject constructor(
//    private val dao: ActivityDao
//) {
//    fun observeAll():           Flow<List<ActivityEntity>> = dao.observeAll()
//    fun observeById(id: Int):   Flow<ActivityEntity?>     = dao.observeById(id)
//    fun observeRunning():       Flow<ActivityEntity?>     = dao.observeRunning()
//
//    suspend fun add(activity: ActivityEntity)             = dao.insert(activity)
//    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)
//    suspend fun update(activity: ActivityEntity)          = dao.update(activity)
//    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) =
//        dao.updateTimer(id, elapsed, running)
//    suspend fun updateStreak(id: Int, streak: Int, date: String) =
//        dao.updateStreak(id, streak, date)
//    suspend fun stopAll()                                 = dao.stopAll()
//    suspend fun delete(activity: ActivityEntity)          = dao.delete(activity)
//    suspend fun clearAll()                                = dao.deleteAll()
//}





import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.dao.SessionDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Singleton
class ActivityRepository @Inject constructor(
    private val dao: ActivityDao,
    private val sessionDao: SessionDao) {
    fun observeAll(): Flow<List<ActivityEntity>> = dao.observeAll()

    fun observeById(id: Int): Flow<ActivityEntity?> = dao.observeById(id)

    fun observeRunning(): Flow<ActivityEntity?> = dao.observeRunning()

    suspend fun add(activity: ActivityEntity): Long = dao.insert(activity)

    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)

    suspend fun update(activity: ActivityEntity) = dao.update(activity)

    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) {
        dao.updateTimer(id, elapsed, running)
    }

    suspend fun updateStreak(id: Int, streak: Int, date: String) {
        dao.updateStreak(id, streak, date)
    }

    suspend fun stopAll() = dao.stopAll()

    suspend fun delete(activity: ActivityEntity) = dao.delete(activity)

    suspend fun clearAll() = dao.deleteAll()

    suspend fun startActivity(activity: ActivityEntity) {
        dao.stopAll()
        dao.updateTimer(
            id = activity.id,
            elapsed = activity.elapsedSeconds,
            running = true
        )
    }

    suspend fun pauseActivity(activity: ActivityEntity) {
        dao.updateTimer(
            id = activity.id,
            elapsed = activity.elapsedSeconds,
            running = false
        )
    }

    suspend fun finishActivity(activity: ActivityEntity, todayLabel: String) {
        dao.updateTimer(
            id = activity.id,
            elapsed = 0L,
            running = false
        )
        dao.updateStreak(
            id = activity.id,
            streak = activity.streakDays + 1,
            date = todayLabel
        )
    }

    suspend fun switchToActivity(next: ActivityEntity) {
        dao.stopAll()
        dao.updateTimer(
            id = next.id,
            elapsed = next.elapsedSeconds,
            running = true
        )
    }

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