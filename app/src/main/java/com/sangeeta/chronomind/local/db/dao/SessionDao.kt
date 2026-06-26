package com.sangeeta.chronomind.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)

    @Query("SELECT * FROM sessions ORDER BY id DESC")
    fun observeAll(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE dateLabel >= :from ORDER BY id DESC")
    fun observeSince(from: String): Flow<List<SessionEntity>>

    @Query("""
        SELECT * FROM sessions
        WHERE activityId = :activityId
          AND dateLabel = :date
          AND isCompleted = 0
        ORDER BY id DESC
        LIMIT 1
    """)
    suspend fun findOpenSession(activityId: Int, date: String): SessionEntity?


    @Query("DELETE FROM sessions")
    suspend fun deleteAll()
}