package com.example.movielist.screens.reminders.ui

import androidx.lifecycle.*
import com.example.movielist.data.RepositoryListener
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.screens.reminders.data.Reminder
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


    private val _screenState = MutableLiveResult<List<Reminder>>().apply { value = Status.InProgress }
    val screenState: LiveResult<List<Reminder>> = _screenState

    private val _movieId = MutableLiveData<Int>()

    val modalScreenState: LiveData<Status<Reminder>> = _movieId
        .switchMap { movieId ->
            liveData {
                when {
                    movieId == DELETED_MOVIE_ID -> emit(Status.Error(Exception("alarm is deleted")))
                    else -> emit(appNotificator.getReminder(movieId))
                }
            }
        }

    fun setReminder(movieId: Int, movieTitle: String, time: Long) {
        viewModelScope.launch {
            appNotificator.setReminder(movieId, movieTitle, time)
            _movieId.postValue(movieId)
        }

    }

    fun unsetReminder(movieId: Int, movieTitle: String) {
        viewModelScope.launch {
            appNotificator.unsetReminder(movieId, movieTitle)
        }
    }

    fun getReminder(movieId: Int) {
        viewModelScope.launch {
            _movieId.postValue(movieId)
        }
    }

    fun getReminders() {
        viewModelScope.launch {
            _screenState.postValue(Status.InProgress)
            _screenState.postValue(appNotificator.getReminders())
        }
    }

    init {
        appNotificator.addListener(this)
    }

    override fun dataChanged() {
        getReminders()
    }

    override fun reminderDeleted(movieId: Int) {}

    override fun reminderAdded(movieId: Int) {
        getReminders()
    }
}