package com.sangeeta.chronomind.local.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sangeeta.chronomind.local.db.dao.ActivityDao
import com.sangeeta.chronomind.local.db.entity.ActivityEntity

//@Database(
//    entities = [ActivityEntity::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class ChronoDatabase : RoomDatabase() {
//    abstract fun activityDao(): ActivityDao
//}




@Database(
    entities      = [ActivityEntity::class],
    version       = 2,            // bumped from 1
    exportSchema  = false
)
abstract class ChronoDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}

// Migration: adds icon + colorHex columns without destroying data
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE activities ADD COLUMN icon TEXT NOT NULL DEFAULT \'📖\'")
        db.execSQL("ALTER TABLE activities ADD COLUMN colorHex TEXT NOT NULL DEFAULT \'#FFCC00\'")
    }
}