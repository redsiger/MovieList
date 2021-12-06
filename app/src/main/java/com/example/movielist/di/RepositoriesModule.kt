package com.example.movielist.di

import com.example.movielist.data.AlarmsDao
import com.example.movielist.data.AlarmsRepository
import com.example.movielist.data.MovieRepository
import com.example.movielist.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieService: MovieService,
//        movieSearchResponseDao: MovieSearchResponseDao
    ): MovieRepository {
//        return MovieRepository(movieService, movieSearchResponseDao)
        return MovieRepository(movieService)
    }

    @Singleton
    @Provides
    fun provideAlarmsRepository(
        alarmsDao: AlarmsDao,
//        movieSearchResponseDao: MovieSearchResponseDao
    ): AlarmsRepository {
//        return MovieRepository(movieService, movieSearchResponseDao)
        return AlarmsRepository(alarmsDao)
    }
}