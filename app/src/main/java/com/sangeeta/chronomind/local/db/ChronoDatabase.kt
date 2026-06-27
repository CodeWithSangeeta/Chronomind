package com.sangeeta.chronomind.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.dao.SessionDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity
import com.sangeeta.chronomind.local.db.entity.SessionEntity

@Database(
    entities = [ActivityEntity::class, SessionEntity::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChronoDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun sessionDao(): SessionDao
}