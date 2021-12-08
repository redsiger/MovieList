package com.example.movielist.screens.movieDetail

import android.util.Log
import androidx.lifecycle.*
import com.example.movielist.screens.movieDetail.credits.Cast
import com.example.movielist.screens.movieDetail.credits.Crew
import com.example.movielist.data.movie.MovieRepository
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MediatorLiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
        private val repository: MovieRepository,
        private val savedStateHandle: SavedStateHandle
): BaseViewModel() {

    /**
     * Variable is representing Screen state
     */
    private val _screenState = MediatorLiveResult<MovieDetailState>().apply { value = Status.InProgress }
    val screenState: LiveResult<MovieDetailState> = _screenState

    /**
     * Input values for Screen state
     */
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

        val movie = _movie.value?.extractData ?: return
        val cast = _cast.value?.extractData ?: return
        val crew = _crew.value?.extractData ?: return
        val recommendations = _recommendations.value?.extractData ?: return

        _screenState.value = Status.Success(MovieDetailState(movie, cast, crew, recommendations))
        Log.e("MERGE SOURCES STATE", _screenState.value.toString())
    }

    init {
        Log.e("INIT", "start")
        _screenState.addSource(_movie) { mergeSources() }
        _screenState.addSource(_cast) { mergeSources() }
        _screenState.addSource(_crew) { mergeSources() }
        _screenState.addSource(_recommendations) { mergeSources() }
        getMovie(movieId)
        getCast(movieId)
        getCrew(movieId)
        getRecommendations(movieId)
        Log.e("INIT", "end")
    }

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

    fun tryAgain() {
        getMovie(movieId)
        getCast(movieId)
        getCrew(movieId)
        getRecommendations(movieId)
    }

    data class MovieDetailState(
        val movie: MovieById,
        val castList: List<Cast>,
        val crewList: List<Crew>,
        val recommendationsList: List<MovieRecommendation>
    )
}

//class MovieDetailViewModelFactory(private val movieId: Int): ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
//            return MovieDetailViewModel(movieId) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//}