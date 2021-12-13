package com.example.movielist.data.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movielist.screens.movieDetail.credits.Cast
import com.example.movielist.screens.movieDetail.credits.CreditsResponse
import com.example.movielist.screens.movieDetail.credits.Crew
import com.example.movielist.screens.movies.MoviesScreen.moviesPaging.MoviesPagingSource
import com.example.movielist.data.Repository
import com.example.movielist.network.movie.Movie
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.MovieService
import com.example.movielist.network.recommentadions.MovieRecommendation
import retrofit2.HttpException
import java.lang.Exception
import com.example.movielist.utils.Status
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val service: MovieService
): Repository {

    fun getPopularMoviesPaging(): Flow<PagingData<Movie>> {
        return  Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(service) }
        ).flow
    }

    suspend fun getSearchResults(searchQuery: String, page: Int): Status<List<Movie>> {
        return try {
            val response = service.getSearchResult(searchQuery, page)
            if (response.isSuccessful) {
                Status.Success(response.body()!!.results)
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: HttpException) {
            return Status.Error(e)
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    /**
     * Function is responsible for providing popular movies
     * from Network API or local database
     */
    suspend fun getPopularMovies(): Status<List<Movie>> {
        return try {
            val response = service.getPopularMovies()
            if (response.isSuccessful) {
                Status.Success(response.body()!!.results)
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: HttpException) {
            return Status.Error(e)
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    /**
     * Function is responsible for providing movie by ID
     * from Network API or local database
     */
    suspend fun getMovie(movieId: Int): Status<MovieById> {
        return try {
            val response = service.getMovie(movieId = movieId)
            if (response.isSuccessful) {
                val responseResult = response.body()!!
                Status.Success(responseResult)
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: HttpException) {
            return Status.Error(e)
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun getCast(movieId: Int): Status<List<Cast>> {
        return getCredits(movieId).proceed {
            it.cast
        }
    }

    suspend fun getCrew(movieId: Int): Status<List<Crew>> {
        return getCredits(movieId).proceed {
            it.crew
        }
    }

    suspend fun getCredits(movieId: Int): Status<CreditsResponse> {
        return try {
            val response = service.getCredits(movieId = movieId)
            if (response.isSuccessful) {
                val responseResult = response.body()!!
                Status.Success(responseResult)
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: HttpException) {
            return Status.Error(e)
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }

    suspend fun getRecommendations(movieId: Int): Status<List<MovieRecommendation>> {
        return try {
            val response = service.getRecommendations(movieId = movieId)
            if (response.isSuccessful) {
                Status.Success(response.body()!!.results)
            } else {
                Status.Error(response.errorBody() as HttpException)
            }
        } catch (e: HttpException) {
            return Status.Error(e)
        } catch (e: Exception) {
            return Status.Error(e)
        }
    }
}