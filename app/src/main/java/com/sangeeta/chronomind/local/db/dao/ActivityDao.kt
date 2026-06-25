package com.sangeeta.chronomind.local.db.dao

import androidx.room.*
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :id")
    fun observeById(id: Int): Flow<ActivityEntity?>

    @Query("SELECT * FROM activities WHERE isRunning = 1 LIMIT 1")
    fun observeRunning(): Flow<ActivityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)

    @Update
    suspend fun update(activity: ActivityEntity)

    @Query("UPDATE activities SET elapsedSeconds = :elapsed, isRunning = :running WHERE id = :id")
    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean)

    @Query("UPDATE activities SET streakDays = :streak, lastActiveDate = :date WHERE id = :id")
    suspend fun updateStreak(id: Int, streak: Int, date: String)

    /**
     * Resets streaks for activities where the user missed yesterday.
     * yesterday: The date string (ISO) for the day before today.
     */
    @Query("""
        UPDATE activities 
        SET streakDays = 0 
        WHERE continueStreakOnMiss = 0 
          AND lastActiveDate < :yesterday 
          AND lastActiveDate != ''
          AND streakDays > 0
    """)
    suspend fun resetMissedStreaks(yesterday: String)

    @Query("UPDATE activities SET isRunning = 0")
    suspend fun stopAll()

    // Pass ImageVector here; Room will use the TypeConverter to save it as String
    @Query("UPDATE activities SET icon = :icon, colorHex = :colorHex WHERE id = :id")
    suspend fun updateAppearance(id: Int, icon: androidx.compose.ui.graphics.vector.ImageVector, colorHex: String)

    @Delete
    suspend fun delete(activity: ActivityEntity)

    @Query("DELETE FROM activities")
    suspend fun deleteAll()
}
