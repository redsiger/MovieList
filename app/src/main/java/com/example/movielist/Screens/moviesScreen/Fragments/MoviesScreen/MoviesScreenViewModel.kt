package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import androidx.lifecycle.viewModelScope
import com.example.movielist.data.MovieRepository
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.network.Movie
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesScreenViewModel @Inject constructor(
    private val repository: MovieRepository
): BaseViewModel() {

    /**
     * Variable is representing Screen state
     */
    var movies = MutableLiveResult<List<Movie>>(Status.InProgress)

    fun getPopularMovies() {
        movies.postValue(Status.InProgress)
        viewModelScope.launch {
            movies.postValue(repository.getPopularMovies())
        }
    }
//    var movies = MutableLiveResult<List<MovieRecommendation>>(Status.InProgress)
//
//    fun getPopularMovies() {
//        movies.postValue(Status.InProgress)
//        viewModelScope.launch {
//            movies.postValue(repository.getRecommendations(550))
//        }
//    }

    fun tryAgain() {
        getPopularMovies()
    }

    init {
        getPopularMovies()
    }

}