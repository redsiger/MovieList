package com.example.movielist.data.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movielist.screens.alarms.Alarm
import com.example.movielist.data.Repository
import com.example.movielist.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmsRepository(
    private val dao: AlarmsDao
): Repository {

    var alarms: LiveData<Status<List<Alarm>>> = liveData{
        emit(getAlarms())
    }

    //        init {
    //            CoroutineScope(Dispatchers.Default).launch {
    //                alarms = getAlarms()
    //            }
    //        }

    //        suspend fun updateAlarms() {
    //            alarms = getAlarms()
    //        }

    suspend fun getAlarms(): Status<List<Alarm>> {
        return try {
            Status.Success(dao.getAlarms())
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun getAlarm(movieId: Int): Status<Alarm> {
        return try {
            val alarm = dao.getAlarm(movieId)
            if (alarm != null) {
                Status.Success(alarm)
            } else {
                Status.Error(Exception())
            }
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