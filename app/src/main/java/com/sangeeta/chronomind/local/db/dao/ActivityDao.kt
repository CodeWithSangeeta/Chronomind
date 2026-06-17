package com.sangeeta.chronomind.local.db.dao

//import androidx.room.*
//import com.sangeeta.chronomind.local.db.entity.ActivityEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface ActivityDao {
//
//    @Query("SELECT * FROM activities ORDER BY orderIndex ASC")
//    fun observeAll(): Flow<List<ActivityEntity>>
//
//    @Query("SELECT * FROM activities WHERE id = :id")
//    fun observeById(id: Int): Flow<ActivityEntity?>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(activity: ActivityEntity): Long
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(activities: List<ActivityEntity>)
//
//    @Update
//    suspend fun update(activity: ActivityEntity)
//
//    @Query("UPDATE activities SET elapsedSeconds = :elapsed, isRunning = :running WHERE id = :id")
//    suspend fun updateTimer(id: Int, elapsed: Long, running: Boolean)
//
//    @Query("UPDATE activities SET streakDays = :streak, lastActiveDate = :date WHERE id = :id")
//    suspend fun updateStreak(id: Int, streak: Int, date: String)
//
//    @Delete
//    suspend fun delete(activity: ActivityEntity)
//
//    @Query("DELETE FROM activities")
//    suspend fun deleteAll()
//}





import androidx.room.*
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :id")
    fun observeById(id: Int): Flow<ActivityEntity?>

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

    @Delete
    suspend fun delete(activity: ActivityEntity)

    @Query("DELETE FROM activities")
    suspend fun deleteAll()

    // ── New in v2 ──────────────────────────────────────────────────────────

    @Query("SELECT * FROM activities WHERE isRunning = 1 LIMIT 1")
    fun observeRunning(): Flow<ActivityEntity?>

    @Query("UPDATE activities SET isRunning = 0")
    suspend fun stopAll()

    @Query("UPDATE activities SET icon = :icon, colorHex = :colorHex WHERE id = :id")
    suspend fun updateAppearance(id: Int, icon: String, colorHex: String)
}