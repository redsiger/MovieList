package com.example.movielist.screens.alarms

import android.util.Log
import androidx.lifecycle.*
import com.example.movielist.data.RepositoryListener
import com.example.movielist.data.alarm.AlarmsRepository
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MediatorLiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DELETED_MOVIE_ID = -1

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    private val repository: AlarmsRepository
): BaseViewModel(), RepositoryListener {

    private val _screenState = MutableLiveResult<List<Alarm>>().apply { value = Status.InProgress }
    val screenState: LiveResult<List<Alarm>> = _screenState

    private val _movieId = MutableLiveData<Int>()

    val modalScreenState: LiveData<Status<Alarm>> = _movieId
        .distinctUntilChanged()
        .switchMap { movieId ->
            liveData {
                when {
                    movieId == DELETED_MOVIE_ID -> emit(Status.Error(Exception("alarm is deleted")))
                    else -> emit(repository.getAlarm(movieId))
                }
            }
        }

    fun getAlarm(movieId: Int) {
        viewModelScope.launch {
            _movieId.postValue(movieId)
        }
    }

//    private val _modalScreenState = MediatorLiveResult<Alarm>().apply { value = Status.InProgress }
//    val modalScreenState: LiveResult<Alarm> = _modalScreenState
//
//    fun getAlarm(movieId: Int) {
//        viewModelScope.launch {
//            _modalScreenState.postValue(Status.InProgress)
//            _modalScreenState.postValue(repository.getAlarm(movieId))
//        }
//    }

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
        repository.addListener(this)
    }

    override fun dataChanged() {
        getAlarms()
    }

    override fun alarmDeleted(movieId: Int) {
        if (movieId == _movieId.value) _movieId.postValue(DELETED_MOVIE_ID)
    }
}