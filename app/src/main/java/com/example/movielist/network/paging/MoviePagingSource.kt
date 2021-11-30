package com.example.movielist.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movielist.network.Movie
import com.example.movielist.network.MovieService
import kotlinx.coroutines.delay
import retrofit2.HttpException

class MoviePagingSource(
    private val service: MovieService,
    private val query: String
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        delay(1500)
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        try {
            val pageNumber: Int = params.key ?: DEFAULT_PAGE_NUMBER
            val pageSize: Int = params.loadSize

            val response = service.getPopularMovies(pageNumber)
            if (response.isSuccessful) {
                val movies = response.body()!!.results
                val nextKey = if (movies.size < DEFAULT_LOAD_SIZE) null else pageNumber + 1
                val prevKey = if (pageNumber == 1) null else pageNumber -1
                return LoadResult.Page(movies, prevKey = prevKey, nextKey = nextKey)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val DEFAULT_PAGE_NUMBER: Int = 1
        const val DEFAULT_LOAD_SIZE: Int = 20
    }
}