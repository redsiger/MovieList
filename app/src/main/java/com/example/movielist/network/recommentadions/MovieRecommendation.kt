package com.example.movielist.network.recommentadions

import android.os.Parcelable
import com.example.movielist.foundation.BaseMovieItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRecommendation(
    @SerializedName("adult")
    override val adult: Boolean,
    @SerializedName("backdrop_path")
    override val backdropPath: String,
    @SerializedName("genre_ids")
    override val genreIds: List<Int>,
    @SerializedName("id")
    override val id: Int,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("original_language")
    override val originalLanguage: String,
    @SerializedName("original_title")
    override val originalTitle: String,
    @SerializedName("overview")
    override val overview: String,
    @SerializedName("popularity")
    override val popularity: Double,
    @SerializedName("poster_path")
    override val posterPath: String,
    @SerializedName("release_date")
    override val releaseDate: String,
    @SerializedName("title")
    override val title: String,
    @SerializedName("video")
    override val video: Boolean,
    @SerializedName("vote_average")
    override val voteAverage: Double,
    @SerializedName("vote_count")
    override val voteCount: Double
): BaseMovieItem, Parcelable