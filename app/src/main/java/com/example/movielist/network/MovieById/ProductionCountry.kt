package com.example.movielist.network.MovieById

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val countryCode: String,
    @SerializedName("name")
    val name: String
): Parcelable