package com.example.movielist.screens.moviesScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movielist.R
import com.example.movielist.databinding.ActivityMainBinding
import com.example.movielist.network.movie.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.startScreen_nav_host) as NavHostFragment
        mBinding.bottomNav.setupWithNavController(navHostFragment.navController)

        val movie = intent.getParcelableExtra<Movie>("movie")
        Log.e("SUCCESS", movie.toString())
    }
}