package com.example.movielist.network.MovieById.VideoResponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoResponse(
    @SerializedName("results")
    val results: List<Trailer>
): Parcelable