package com.example.movielist.data.reminder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.screens.reminders.data.Reminder

@Dao
interface RemindersDao {

    @Query("SELECT * FROM reminders")
    suspend fun getReminders(): List<Reminder>

    @Query("SELECT * FROM reminders WHERE movieId = :movieId")
    suspend fun getReminder(movieId: Int): Reminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE movieId = :movieId")
    suspend fun deleteReminder(movieId: Int)
}