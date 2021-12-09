package com.example.movielist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.data.alarm.AlarmsDao

@Database(entities = [
    Reminder::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAlarmsDao(): AlarmsDao

    companion object {

        @Volatile
        private var database: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                ).build()
                database as AppDatabase
            } else database as AppDatabase
        }
    }
}