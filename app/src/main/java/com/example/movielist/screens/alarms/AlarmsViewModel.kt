package com.example.movielist.screens.alarms

import androidx.lifecycle.*
import com.example.movielist.data.RepositoryListener
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.utils.AppNotificator
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DELETED_MOVIE_ID = -1

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    private val appNotificator: AppNotificator
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
                    else -> emit(appNotificator.getAlarm(movieId))
                }
            }
        }

    fun getAlarm(movieId: Int) {
        viewModelScope.launch {
            _movieId.postValue(movieId)
        }
    }

    fun getAlarms() {
        viewModelScope.launch {
            _screenState.postValue(Status.InProgress)
            _screenState.postValue(appNotificator.getAlarms())
        }
    }

    init {
        appNotificator.addListener(this)
    }

    override fun dataChanged() {
        getAlarms()
    }

    override fun alarmDeleted(movieId: Int) {
        if (movieId == _movieId.value) _movieId.postValue(DELETED_MOVIE_ID)
    }

    override fun alarmAdded(movieId: Int) {
        getAlarms()
    }
}