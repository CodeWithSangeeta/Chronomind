package com.sangeeta.chronomind.repository

import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepository @Inject constructor(
    private val dao: ActivityDao
) {
    fun observeAll(): Flow<List<ActivityEntity>> = dao.observeAll()

    fun observeById(id: Int): Flow<ActivityEntity?> = dao.observeById(id)

    suspend fun addActivity(activity: ActivityEntity) = dao.insert(activity)

    suspend fun seedActivities(activities: List<ActivityEntity>) = dao.insertAll(activities)

    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean) =
        dao.updateTimer(id, elapsed, running)

    suspend fun updateStreak(id: Int, streak: Int, date: String) =
        dao.updateStreak(id, streak, date)

    suspend fun deleteActivity(activity: ActivityEntity) = dao.delete(activity)

    suspend fun clearAll() = dao.deleteAll()
}