package com.example.movielist.data.alarm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movielist.R
import com.example.movielist.screens.alarms.Alarm
import com.example.movielist.data.Repository
import com.example.movielist.data.RepositoryListener
import com.example.movielist.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmsRepository(
    private val dao: AlarmsDao,
    private val context: Context
): Repository {

    private var listeners: MutableSet<RepositoryListener> = mutableSetOf()
    fun addListener(listener: RepositoryListener) {
        listeners.add(listener)
        listeners.forEach { update() }
    }

    private fun update() { listeners.forEach { it.dataChanged() } }
    private fun alarmDeleted(movieId: Int) { listeners.forEach { it.alarmDeleted(movieId) } }
    private fun alarmAdded(movieId: Int) { listeners.forEach { it.alarmAdded(movieId) } }


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
                val s: String = context.resources.getString(R.string.alarm_not_found_text)
                Status.Error(Exception(s))
            }
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun deleteAlarm(movieId: Int) {
        dao.deleteAlarm(movieId)
        alarmDeleted(movieId)
        update()
    }

    suspend fun addAlarm(alarm: Alarm) {
        dao.addAlarm(alarm)
        alarmAdded(alarm.movieId)
        update()
    }
}