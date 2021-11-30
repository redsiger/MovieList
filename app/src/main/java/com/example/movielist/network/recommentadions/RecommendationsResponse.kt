package com.example.movielist.network.recommentadions


import com.google.gson.annotations.SerializedName

data class RecommendationsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieRecommendation>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)