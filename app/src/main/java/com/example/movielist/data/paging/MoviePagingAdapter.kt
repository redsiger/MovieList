package com.example.movielist.data.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movielist.R
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.movieItem.MovieViewHolder
import com.example.movielist.network.movie.Movie
import com.squareup.picasso.Picasso

class MoviePagingAdapter(private val mPicasso: Picasso) : PagingDataAdapter<Movie, MovieViewHolder>(
    MOVIE_COMPARATOR
) {

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movie = getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, mPicasso)
    }
}

