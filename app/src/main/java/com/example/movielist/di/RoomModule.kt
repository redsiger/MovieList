package com.example.movielist.di

import android.content.Context
import com.example.movielist.data.reminder.RemindersDao
import com.example.movielist.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAlarmsDao(@ApplicationContext context: Context): RemindersDao {
        return AppDatabase.getInstance(context).getAlarmsDao()
    }
}