package com.example.movielist.Screens.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.movielist.Screens.moviesScreen.MainActivity
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
        startActivity(Intent(this, MainActivity::class.java))
        finish()
//        Toast.makeText(this, GENRES.toString(), Toast.LENGTH_LONG).show()
    }
}