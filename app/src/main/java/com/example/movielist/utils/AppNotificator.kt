package com.example.movielist.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.movielist.R
import com.example.movielist.screens.alarms.Alarm
import com.example.movielist.screens.moviesScreen.MainActivity
import com.example.movielist.data.alarm.AlarmsRepository

class AppNotificator(private val mContext: Context,
                     private val mAlarmManager: AlarmManager,
                     private val mNotificationManagerCompat: NotificationManagerCompat,
                     private val mAlarmsRepository: AlarmsRepository) {

//    suspend fun updateAlarms() {
//        mAlarmsRepository.updateAlarms()
//    }

    suspend fun setNotification(movieId: Int, movieTitle: String, time: Long) {
        val pendingIntent = createPendingIntent(movieId, movieTitle)

        val sdk = Build.VERSION.SDK_INT
        when {
            sdk >= Build.VERSION_CODES.M -> {
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
            sdk >= Build.VERSION_CODES.LOLLIPOP -> {
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
            else -> {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
        }

        createAlarm(movieId, movieTitle, time)
    }

    suspend fun unsetNotification(movieId: Int, movieTitle: String) {
        val pendingIntent = createPendingIntent(movieId, movieTitle)
        mAlarmManager.cancel(pendingIntent)
        deleteAlarm(movieId)
    }

    private fun createPendingIntent(movieId: Int, movieTitle: String): PendingIntent {
        val intent = Intent(mContext, AlarmReceiver::class.java)
        intent.action = "PUSH_NOTIFICATION $movieId"
        intent.putExtra("movieId", movieId)
        intent.putExtra("movieTitle", movieTitle)

        return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    suspend fun createAlarm(movieId: Int, movieTitle: String, time: Long) {
        mAlarmsRepository.addAlarm(
                Alarm(
                        movieId = movieId,
                        movieTitle = movieTitle,
                        time = time
                )
        )
        Log.e("APP_NOTIFICATOR", "alarm created")
    }
    suspend fun createAlarm(alarm: Alarm) {
        mAlarmsRepository.addAlarm(alarm)
    }

    suspend fun deleteAlarm(movieId: Int) {
        mAlarmsRepository.deleteAlarm(movieId)
    }

    suspend fun pushNotificationAndDeleteAlarm(
            channel: String,
            title: String,
            bundle: Bundle,
            notificationId: Int,
    movieId: Int) {
        pushNotification(channel, title, bundle, notificationId)
        deleteAlarm(movieId)
    }

    fun pushNotification(
            channel: String,
            title: String,
            bundle: Bundle,
            notificationId: Int,
    ) {
        val pendingIntent = NavDeepLinkBuilder(mContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_graph)
                .addDestination(R.id.movie_detail_graph, bundle)
                .createPendingIntent()

        val builder = NotificationCompat.Builder(mContext, channel)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setStyle(
                        NotificationCompat.BigTextStyle()
                                .bigText(mContext.getString(R.string.app_notificationRemind_text))
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notification = builder.build()

        mNotificationManagerCompat.notify(notificationId, notification)
    }

    fun deleteNotification(notificationId: Int) {
        mNotificationManagerCompat.cancel(notificationId)
    }
}