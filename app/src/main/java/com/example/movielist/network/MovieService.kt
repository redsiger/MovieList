package com.example.movielist.network

import com.example.movielist.Screens.movieDetail.credits.CreditsResponse
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.recommentadions.RecommendationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    //    https://api.themoviedb.org/3/movie/popular?api_key=bfe801649ca860d496a1b7a533405418
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): Response<MovieResponse>

    //    https://api.themoviedb.org/3/genre/movie/list?api_key=bfe801649ca860d496a1b7a533405418
    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenresResponse>

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") movieId: Int): Response<MovieById>

    @GET("movie/{id}/credits")
    suspend fun getCredits(@Path("id") movieId: Int): Response<CreditsResponse>

    @GET("movie/{id}/recommendations")
    suspend fun getRecommendations(@Path("id") movieId: Int): Response<RecommendationsResponse>
}