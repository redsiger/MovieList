package com.example.movielist.network.MovieById.VideoResponse

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("key")
    val key: String
)
