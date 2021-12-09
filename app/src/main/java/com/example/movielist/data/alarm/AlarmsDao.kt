package com.example.movielist.data.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.screens.reminders.data.Reminder

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM reminders")
    suspend fun getAlarms(): List<Reminder>

    @Query("SELECT * FROM reminders WHERE movieId = :movieId")
    suspend fun getAlarm(movieId: Int): Reminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE movieId = :movieId")
    suspend fun deleteAlarm(movieId: Int)
}