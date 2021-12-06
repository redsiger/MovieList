package com.example.movielist.Screens.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.movielist.Screens.moviesScreen.MainActivity
import com.example.movielist.network.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashScreenActivity: AppCompatActivity() {

    private val mViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            mViewModel.initGenres()
        }

        val intent = Intent(this, MainActivity::class.java)
        val movie = Movie(
            adult = false,
            backdropPath = "null",
            genreIds = emptyList(),
            id = 0,
            originalLanguage = "Language",
            originalTitle = "orTitle",
            overview = "overView",
            popularity = 2.0,
            posterPath = "null",
            releaseDate = "132",
            title = "Mtitle",
            video = false,
            voteAverage = 2.0,
            voteCount = 3.0
        )
        intent.putExtra("movie", movie)
        startActivity(intent)
        finish()
    }
}