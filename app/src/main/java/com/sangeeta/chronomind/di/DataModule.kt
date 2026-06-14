package com.sangeeta.chronomind.di

import com.sangeeta.chronomind.local.db.ChronoDatabase
import com.sangeeta.chronomind.local.db.dao.ActivityDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChronoDatabase =
        Room.databaseBuilder(
            context,
            ChronoDatabase::class.java,
            "chrono_db"
        ).build()

    @Provides
    @Singleton
    fun provideActivityDao(db: ChronoDatabase): ActivityDao = db.activityDao()
}