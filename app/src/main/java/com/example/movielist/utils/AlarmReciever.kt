package com.example.movielist.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.os.bundleOf
import com.example.movielist.di.CHANNEL_1_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MOVIE_ID_DEFAULT_VALUE: Int = -1
const val MOVIE_TITLE_DEFAULT_VALUE: String = "MOVIE_TITLE_DEFAULT_VALUE"

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject lateinit var mAppNotificator: AppNotificator

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("onReceive", "START")
        val movieId: Int = intent.getIntExtra("movieId", MOVIE_ID_DEFAULT_VALUE)
        val movieTitle: String = intent.getStringExtra("movieTitle") ?: MOVIE_TITLE_DEFAULT_VALUE
        Log.e("onReceive", "$movieId")
        Log.e("onReceive", movieTitle)


        if (movieId != MOVIE_ID_DEFAULT_VALUE && movieTitle != MOVIE_TITLE_DEFAULT_VALUE) {
            CoroutineScope(Dispatchers.Default).launch {
                mAppNotificator.pushNotificationAndDeleteAlarm(
                        CHANNEL_1_ID,
                        movieTitle,
                        bundleOf("movieId" to movieId),
                        movieId,
                        movieId
                )
//                mAppNotificator.updateAlarms()
            }
        }
        Log.e("onReceive", "END")
    }
}