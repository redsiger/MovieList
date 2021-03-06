package com.example.movielist.screens.movieDetail.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.movielist.data.RepositoryListener
import com.example.movielist.screens.movieDetail.credits.Cast
import com.example.movielist.screens.movieDetail.credits.Crew
import com.example.movielist.data.movie.MovieRepository
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MediatorLiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.utils.AppNotificator
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
        private val repository: MovieRepository,
        private val appNotificator: AppNotificator,
        private val savedStateHandle: SavedStateHandle
): BaseViewModel(), RepositoryListener {

    /**
     * Variable is representing Screen state
     */
    private val _screenState = MediatorLiveResult<MovieDetailState>().apply { value = Status.InProgress }
    val screenState: LiveResult<MovieDetailState> = _screenState

    /**
     * Input values for Screen state
     */
    private val _alarm = MutableLiveResult<Reminder>(Status.InProgress)
    private val _movie = MutableLiveResult<MovieById>(Status.InProgress)
    private val _cast = MutableLiveResult<List<Cast>>(Status.InProgress)
    private val _crew = MutableLiveResult<List<Crew>>(Status.InProgress)
    private val _recommendations = MutableLiveResult<List<MovieRecommendation>>(Status.InProgress)
    val movieId by lazy {
        savedStateHandle.get<Int>("movieId")!!
    }

    private fun mergeSources() {

        if (_movie.value is Status.Error
            || _cast.value is Status.Error
            || _crew.value is Status.Error
            || _recommendations.value is Status.Error) {
                _screenState.value = Status.Error(Exception())
                return
        }

        val alarm = _alarm.value?.extractData
        val movie = _movie.value?.extractData ?: return
        val cast = _cast.value?.extractData ?: return
        val crew = _crew.value?.extractData ?: return
        val recommendations = _recommendations.value?.extractData ?: return

        _screenState.value = Status.Success(MovieDetailState(alarm, movie, cast, crew, recommendations))
    }

    init {
        Log.e("INIT", "start")
        appNotificator.addListener(this)
        getAlarm(movieId)
        getMovie(movieId)
        getCast(movieId)
        getCrew(movieId)
        getRecommendations(movieId)
        _screenState.addSource(_alarm) { mergeSources() }
        _screenState.addSource(_movie) { mergeSources() }
        _screenState.addSource(_cast) { mergeSources() }
        _screenState.addSource(_crew) { mergeSources() }
        _screenState.addSource(_recommendations) { mergeSources() }
        Log.e("INIT", "end")
    }

    fun getAlarm(movieId: Int) {
        viewModelScope.launch {
            _alarm.postValue(appNotificator.getReminder(movieId))
        }
    }

//    fun getAlarm(movieId: Int) {
//        viewModelScope.launch {
//            val response = appNotificator.getAlarm(movieId)
//            if (response.extractData is Alarm) {
//                _alarm.postValue(response)
//                _alarmIsSet.postValue(true)
//            } else {
//                _alarm.postValue(Status.InProgress)
//                _alarmIsSet.postValue(false)
//            }
//        }
//    }

    fun getMovie(movieId: Int) {
        _movie.postValue(Status.InProgress)
        viewModelScope.launch {
            _movie.postValue(repository.getMovie(movieId))
        }
    }

    fun getCast(movieId: Int) {
        _cast.postValue(Status.InProgress)
        viewModelScope.launch {
            _cast.postValue(repository.getCast(movieId))
        }
    }

    fun getCrew(movieId: Int) {
        _crew.postValue(Status.InProgress)
        viewModelScope.launch {
            _crew.postValue(repository.getCrew(movieId))
        }
    }

    fun getRecommendations(movieId: Int) {
        _recommendations.postValue(Status.InProgress)
        viewModelScope.launch {
            _recommendations.postValue(repository.getRecommendations(movieId))
        }
    }

    fun setNotification(movieId: Int, movieTitle: String, time: Long) {
        viewModelScope.launch {
            appNotificator.setReminder(movieId, movieTitle, time)
        }
    }

    fun unsetNotification(movieId: Int, movieTitle: String) {
        viewModelScope.launch {
            appNotificator.unsetReminder(movieId, movieTitle)
        }
    }

    fun tryAgain() {
        getMovie(movieId)
        getCast(movieId)
        getCrew(movieId)
        getRecommendations(movieId)
    }

    data class MovieDetailState(
        val reminder: Reminder?,
        val movie: MovieById,
        val castList: List<Cast>,
        val crewList: List<Crew>,
        val recommendationsList: List<MovieRecommendation>
    )

    override fun dataChanged() {}

    override fun reminderDeleted(movieId: Int) {
        if (movieId == movieId) getAlarm(movieId)
    }

    override fun reminderAdded(movieId: Int) {
        if (movieId == movieId) getAlarm(movieId)
    }
}
