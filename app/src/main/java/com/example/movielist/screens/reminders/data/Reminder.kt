package com.example.movielist.screens.reminders.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val movieTitle: String,
    val time: Long,
    val selected: Boolean = false
): Parcelable