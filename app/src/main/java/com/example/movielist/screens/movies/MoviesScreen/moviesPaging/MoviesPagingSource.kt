package com.example.movielist.screens.movies.MoviesScreen.moviesPaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movielist.network.movie.Movie
import com.example.movielist.network.MovieService
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE = 1

class MoviesPagingSource(
    private val service: MovieService
): PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: START_PAGE
        try {
            val response = service.getPopularMovies(page)
            if (response.isSuccessful) {
                val movies = response.body()!!.results
                val nextPage = if (response.body()!!.totalPages == page) null
                else page.plus(1)
                return LoadResult.Page(
                    data = movies,
                    prevKey = if (page == START_PAGE) null else page.minus(1),
                    nextKey = nextPage
                )
            } else {
                return LoadResult.Error(response.errorBody() as HttpException)
            }
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
