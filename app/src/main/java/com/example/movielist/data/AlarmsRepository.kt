package com.example.movielist.data

import com.example.movielist.Screens.alarms.Alarm
import com.example.movielist.utils.Status

class AlarmsRepository(
    private val dao: AlarmsDao
): Repository {

    suspend fun getAlarms(): Status<List<Alarm>> {
        return try {
            Status.Success(dao.getAlarms())
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun getAlarm(movieId: Int): Status<Alarm> {
        return try {
            Status.Success(dao.getAlarm(movieId))
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun deleteAlarm(movieId: Int) {
        dao.deleteAlarm(movieId)
    }

    suspend fun addAlarm(alarm: Alarm) {
        dao.addAlarm(alarm)
    }
}