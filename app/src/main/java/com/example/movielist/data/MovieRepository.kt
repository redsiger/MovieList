package com.example.movielist.data

import com.example.movielist.Screens.movieDetail.credits.Cast
import com.example.movielist.Screens.movieDetail.credits.CreditsResponse
import com.example.movielist.Screens.movieDetail.credits.Crew
import com.example.movielist.network.Movie
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.MovieService
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.network.recommentadions.RecommendationsResponse
import retrofit2.HttpException
import java.lang.Exception
import com.example.movielist.utils.Status

class MovieRepository(
    private val service: MovieService
): Repository {

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
//    suspend fun getPopularMovies(): Status<List<Movie>> {
//        var result: Status<List<Movie>> = Status.Empty
//        try {
//            val response = service.getPopularMovies()
//            if (response.isSuccessful) {
//                val responseResult = response.body()!!.results
//                result = Status.Success(responseResult)
//            } else {
//                result = Status.Error(response.errorBody() as HttpException)
//            }
//        } catch (e: HttpException) {
//            return Status.Error(e)
//        } catch (e: Exception) {
//            return Status.Error(e)
//        }
//
//        return if (result.extractData!!.isEmpty()) {
//            Status.Error(exception = Exception("List is empty"))
//        } else result
//    }

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