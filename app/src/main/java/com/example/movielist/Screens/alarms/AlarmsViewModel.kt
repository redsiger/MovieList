package com.example.movielist.Screens.alarms

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.AlarmsRepository
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MediatorLiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    private val repository: AlarmsRepository
): BaseViewModel() {

    private val _screenState = MutableLiveResult<List<Alarm>>().apply { value = Status.InProgress }
    val screenState: LiveResult<List<Alarm>> = _screenState

    private val _modalScreenState = MediatorLiveResult<Alarm>().apply { value = Status.InProgress }
    val modalScreenState: LiveResult<Alarm> = _modalScreenState

    fun getAlarm(movieId: Int) {
        viewModelScope.launch {
            _modalScreenState.postValue(Status.InProgress)
            _modalScreenState.postValue(repository.getAlarm(movieId))
        }
    }

    fun getAlarms() {
        viewModelScope.launch {
            _screenState.postValue(Status.InProgress)
            _screenState.postValue(repository.getAlarms())
        }
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.addAlarm(alarm)
            getAlarms()
        }
    }

    fun deleteAlarm(movieId: Int) {
        viewModelScope.launch {
            repository.deleteAlarm(movieId)
            getAlarms()
        }
    }

    init {
        Log.e("AlarmsViewModel", "CREATE")
        getAlarms()
    }
}