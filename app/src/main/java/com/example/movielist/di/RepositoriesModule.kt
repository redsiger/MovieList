package com.example.movielist.di

import android.content.Context
import com.example.movielist.data.alarm.AlarmsDao
import com.example.movielist.data.alarm.AlarmsRepository
import com.example.movielist.data.movie.MovieRepository
import com.example.movielist.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideMovieRepository(movieService: MovieService): MovieRepository
    = MovieRepository(movieService)

    @Singleton
    @Provides
    fun provideAlarmsRepository(alarmsDao: AlarmsDao, context: Context): AlarmsRepository
    = AlarmsRepository(alarmsDao, context)

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}