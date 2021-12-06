package com.example.movielist.Screens.search

import android.util.Log
import androidx.lifecycle.*
import com.example.movielist.data.MovieRepository
import com.example.movielist.di.GENRES
import com.example.movielist.foundation.BaseViewModel
import com.example.movielist.foundation.LiveResult
import com.example.movielist.foundation.MutableLiveResult
import com.example.movielist.network.Genre
import com.example.movielist.network.Movie
import com.example.movielist.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel  @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private var _searchResult = MutableLiveResult<List<Movie>>(Status.InProgress)
    val searchResult: LiveResult<List<Movie>> = _searchResult

    val genres: LiveData<Map<Int, Int>> = _searchResult
        .switchMap { status ->
            liveData {
                val movies = status.extractData ?: emptyList()
                val genreIdsMap = mutableMapOf<Int, Int>()
                movies.forEach { movie ->
                    movie.genreIds.forEach { genreId ->
                        if (genreIdsMap.containsKey(genreId)) {
                            genreIdsMap[genreId] = genreIdsMap[genreId]!! + 1
                        } else genreIdsMap[genreId] = 1
                    }
                }
                Log.e("genres", genreIdsMap.toString())
                emit(genreIdsMap)
            }
        }

    private val searchQuery: String? by lazy {
        savedStateHandle.get("searchQuery")
    }

    fun getSearchResult() {
        viewModelScope.launch {
            searchQuery?.let { searchQuery ->
                _searchResult.postValue(Status.InProgress)
                _searchResult.postValue(repository.getSearchResults(searchQuery))
            }
        }
    }



    init {
        getSearchResult()

//        genres = _searchResult
//            .switchMap { status ->
//                liveData {
//                    val movies = status.extractData ?: emptyList()
//                    val genreIdsMap = mutableMapOf<Int, Int>()
//                    movies.forEach { movie ->
//                        movie.genreIds.forEach { genreId ->
//                            if (genreIdsMap.containsKey(genreId)) {
//                                genreIdsMap[genreId] = genreIdsMap[genreId]!! + 1
//                            } else genreIdsMap[genreId] = 1
//                        }
//                    }
//                    Log.e("genres", genreIdsMap.toString())
//                    emit(genreIdsMap)
//                }
//            }


    }
}