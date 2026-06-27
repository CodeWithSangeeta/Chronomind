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

    @Query("SELECT * FROM activities WHERE hasPendingSession = 1 AND pendingSessionDate != :today AND pendingSessionDate != ''")
    suspend fun getStalePendingSessions(today: String): List<ActivityEntity>

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

    @Query("""
        UPDATE activities
        SET hasPendingSession = :hasPending, pendingSessionDate = :pendingDate
        WHERE id = :id
    """)
    suspend fun updatePendingFlag(id: Int, hasPending: Boolean, pendingDate: String)

    @Query("""
    UPDATE activities
    SET elapsedSeconds = 0,
        isRunning = 0,
        hasPendingSession = 0,
        pendingSessionDate = '',
        sessionStartedAtEpochMillis = NULL,
        sessionEndsAtEpochMillis = NULL,
        accumulatedElapsedBeforeStartSeconds = 0
    WHERE id = :id
""")
    suspend fun resetSession(id: Int)

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


    @Query("""
    UPDATE activities
    SET isRunning = 1,
        sessionStartedAtEpochMillis = :startedAt,
        sessionEndsAtEpochMillis = :endsAt,
        accumulatedElapsedBeforeStartSeconds = :accumulatedElapsed,
        hasPendingSession = 0,
        pendingSessionDate = ''
    WHERE id = :id
""")
    suspend fun startSession(
        id: Int,
        startedAt: Long,
        endsAt: Long?,
        accumulatedElapsed: Long
    )

    @Query("""
    UPDATE activities
    SET elapsedSeconds = :elapsedSeconds,
        isRunning = 0,
        sessionStartedAtEpochMillis = NULL,
        sessionEndsAtEpochMillis = NULL,
        accumulatedElapsedBeforeStartSeconds = :elapsedSeconds,
        hasPendingSession = 1,
        pendingSessionDate = :pendingDate
    WHERE id = :id
""")
    suspend fun pauseSession(
        id: Int,
        elapsedSeconds: Long,
        pendingDate: String
    )

    @Query("""
    UPDATE activities
    SET elapsedSeconds = :elapsedSeconds,
        isRunning = :running
    WHERE id = :id
""")
    suspend fun updateElapsedSnapshot(
        id: Int,
        elapsedSeconds: Long,
        running: Boolean
    )
}
