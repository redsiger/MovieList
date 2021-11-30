package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movielist.di.GENRES
import com.example.movielist.di.GENRES_NOT_LOADED
import com.example.movielist.network.Movie
import com.example.movielist.network.paging.MoviePagingSource
import com.example.movielist.network.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesPagingScreenViewModel @Inject constructor(
    private val service: MovieService
//    private val pagingSource: MoviePagingSource
): ViewModel() {

    val movies: StateFlow<PagingData<Movie>> = Pager(
        PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
        ),
        pagingSourceFactory = { MoviePagingSource(service, QUERY) }
    ).flow
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun isGenresLoaded() {
        if (GENRES[-1] == GENRES_NOT_LOADED) {
            viewModelScope.launch {
                service.getGenres()
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE: Int = 20
        const val QUERY: String = "popular"
    }
}