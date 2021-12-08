package com.example.movielist.data.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.screens.alarms.Alarm

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    suspend fun getAlarms(): List<Alarm>

    @Query("SELECT * FROM alarms WHERE movieId = :movieId")
    suspend fun getAlarm(movieId: Int): Alarm

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE movieId = :movieId")
    suspend fun deleteAlarm(movieId: Int)
}