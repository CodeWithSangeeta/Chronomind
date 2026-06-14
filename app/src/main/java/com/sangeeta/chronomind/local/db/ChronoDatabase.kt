package com.sangeeta.chronomind.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity

@Database(
    entities = [ActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChronoDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}