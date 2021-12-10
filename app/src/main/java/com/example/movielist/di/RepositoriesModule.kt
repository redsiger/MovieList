package com.example.movielist.di

import android.content.Context
import com.example.movielist.data.reminder.RemindersDao
import com.example.movielist.data.reminder.RemindersRepository
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
    fun provideAlarmsRepository(remindersDao: RemindersDao, context: Context): RemindersRepository
    = RemindersRepository(remindersDao, context)

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}