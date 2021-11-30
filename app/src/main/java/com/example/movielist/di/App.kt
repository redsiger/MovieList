package com.example.movielist.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.example.movielist.R
import dagger.hilt.android.HiltAndroidApp
import java.util.*

const val CHANNEL_1_ID: String = "com.android.example.MovieList.notificationChannel.movieReminder"
lateinit var LOCALE: Locale
lateinit var LANGUAGE: String
lateinit var REGION: String
lateinit var TRAILERS: String
lateinit var GENRES: MutableMap<Int, String>
const val GENRES_LOADED: String = "GENRES_LOADED"
const val GENRES_NOT_LOADED: String = "GENRES_NOT_LOADED"

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        defineSystemLanguage()
        initGenres()
    }

    private fun initGenres() {
        GENRES = mutableMapOf()
        GENRES[-1] = GENRES_NOT_LOADED
    }

    private fun defineSystemLanguage() {
        LOCALE = Locale.getDefault()
        LANGUAGE = LOCALE.language
        REGION = LOCALE.country
        TRAILERS = "videos"
//        Log.e("locale", LANGUAGE)
//        Log.e("locale", REGION)
//        Log.e("TRAILERS", TRAILERS)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_1_ID
            val channelName = "Movie reminders"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channel1 = NotificationChannel(channelId, channelName, channelImportance)
            channel1.description = getString(R.string.notification_channel_moviesNotification_desc)

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
        }
    }
}