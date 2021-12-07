package com.example.movielist.data.alarm

import com.example.movielist.Screens.alarms.Alarm
import com.example.movielist.data.Repository
import com.example.movielist.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmsRepository(
    private val dao: AlarmsDao
): Repository {

    var alarms: Status<List<Alarm>> = Status.InProgress

    init {
        CoroutineScope(Dispatchers.Default).launch {
            alarms = getAlarms()
        }
    }

    suspend fun updateAlarms() {
        alarms = getAlarms()
    }

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