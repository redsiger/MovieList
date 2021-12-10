package com.example.movielist.di

import android.app.AlarmManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.example.movielist.data.reminder.RemindersRepository
import com.example.movielist.utils.AppNotificator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideAppNotificator(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
        notificationManagerCompat: NotificationManagerCompat,
        remindersRepository: RemindersRepository
    ): AppNotificator = AppNotificator(context, alarmManager, notificationManagerCompat, remindersRepository)


    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat
    = NotificationManagerCompat.from(context)

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager
    = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
}