package com.example.movielist.Screens.alarms

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val movieTitle: String,
    val time: Long,
    val selected: Boolean = false
): Parcelable