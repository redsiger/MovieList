package com.example.movielist.Screens.splashScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.movielist.di.GENRES
import com.example.movielist.di.GENRES_LOADED
import com.example.movielist.network.Genre
import com.example.movielist.network.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val service: MovieService
): ViewModel() {

    suspend fun initGenres() {
        try {
            val response = service.getGenres()
            if (response.isSuccessful) {
                val genresList: List<Genre> = response.body()?.genres ?: emptyList()
                genresList.forEach { genre ->
                    GENRES[genre.id] = genre.name
                }
                GENRES[-1] = GENRES_LOADED
                Log.e("GENRES", "RESPONSE IS SUCCESSFUL")

            }
            else {
                Log.e("GENRES", "RESPONSE NOT SUCCESSFUL")
            }
        } catch (e: HttpException) {
            Log.e("Splash1", e.message())
        } catch (e: Exception) {
            Log.e("Splash2", e.toString())
        }
    }
}