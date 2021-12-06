package com.example.movielist.foundation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

interface BaseMovieItem: Parcelable {
    abstract val adult: Boolean
    abstract val backdropPath: String
    abstract val genreIds: List<Int>
    abstract val id: Int
    abstract val originalLanguage: String
    abstract val originalTitle: String
    abstract val overview: String
    abstract val popularity: Double
    abstract val posterPath: String
    abstract val releaseDate: String
    abstract val title: String
    abstract val video: Boolean
    abstract val voteAverage: Double
    abstract val voteCount: Double
}