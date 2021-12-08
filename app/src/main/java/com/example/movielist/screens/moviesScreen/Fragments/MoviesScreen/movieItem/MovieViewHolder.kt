package com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.movieItem

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.databinding.ItemMovieBinding
import com.example.movielist.di.GENRES
import com.example.movielist.di.TMDB_IMG_URL
import com.example.movielist.foundation.BaseMovieItem
import com.squareup.picasso.Picasso
import java.math.BigDecimal
import java.math.RoundingMode


/**
 * Class responsible for inflating MovieItem for RecyclerView
 */
class MovieViewHolder(
    itemView: View,
    private val mPicasso: Picasso
) : RecyclerView.ViewHolder(itemView) {

    private val mBinding = ItemMovieBinding.bind(itemView)


    fun bind(movie: BaseMovieItem) {
        with(mBinding) {
            setPoster(movie)
            itemMovieTitle.text = movie.title
            setRating(movie)
            setRatingColor(movie)
            setGenre(movie)
        }
    }

    private fun setRating(movie: BaseMovieItem) {
        with(mBinding) {
            val digits = 1
            itemMovieRating.text = BigDecimal(movie.voteAverage)
                .setScale(digits, RoundingMode.HALF_EVEN)
                .toString()

//            try {
//                val format = DecimalFormat("#.#")
//                format.roundingMode = RoundingMode.CEILING
//                itemMovieRating.text = format.format(movie.voteAverage)
//            } catch (e: NumberFormatException) {
//                Log.e("NumberFormatException", movie.title)
//                itemMovieRating.text = movie.voteAverage.toString()
//            }

        }
    }

    private fun setRatingColor(movie: BaseMovieItem) {
        with(mBinding) {
            when {
                movie.voteAverage >= 7 -> {
                    itemMovieRatingCard.setBackgroundResource(R.color.item_movie_rating_high)
                }
                movie.voteAverage >= 6 -> {
                    itemMovieRatingCard.setBackgroundResource(R.color.item_movie_rating_medium)
                }
                else -> {
                    itemMovieRatingCard.setBackgroundResource(R.color.item_movie_rating_low)
                }
            }
        }
    }

    private fun setPoster(movie: BaseMovieItem) {
        with(mBinding) {
            if (movie.posterPath != "null") {
                mPicasso
                    .load(TMDB_IMG_URL + movie.posterPath)
                    .resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height)
                    .into(itemMovieImg)
            } else {
                itemMovieImg.setImageResource(R.drawable.item_movie_placeholder)
            }
        }
    }

    private fun setGenre(movie: BaseMovieItem) {
        with(mBinding) {
            if (movie.genreIds.isNotEmpty()) {
                val genreId = movie.genreIds[0]
                if (GENRES.containsKey(genreId)) {
                    itemMovieGenre.text = GENRES[genreId]
                } else {
                    itemMovieGenre.text = "---"
                }
            }
        }
    }
}