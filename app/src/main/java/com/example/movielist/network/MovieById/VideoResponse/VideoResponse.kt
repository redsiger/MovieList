package com.example.movielist.network.MovieById.VideoResponse

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results")
    val results: List<Trailer>
)