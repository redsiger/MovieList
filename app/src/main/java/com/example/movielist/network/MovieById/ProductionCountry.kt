package com.example.movielist.network.MovieById

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val countryCode: String,
    @SerializedName("name")
    val name: String
)