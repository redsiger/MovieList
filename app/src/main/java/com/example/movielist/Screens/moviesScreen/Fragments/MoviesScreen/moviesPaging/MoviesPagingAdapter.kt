package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.moviesPaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movielist.R
import com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.movieItem.MovieViewHolder
import com.example.movielist.foundation.BaseMovieItem
import com.example.movielist.network.Movie
import com.squareup.picasso.Picasso

class MoviesPagingAdapter(
    private val picasso: Picasso
    ): PagingDataAdapter<BaseMovieItem, MovieViewHolder>(MOVIE_COMPARATOR) {

        companion object {
            private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<BaseMovieItem>() {
                override fun areItemsTheSame(oldItem: BaseMovieItem, newItem: BaseMovieItem): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: BaseMovieItem, newItem: BaseMovieItem): Boolean {
                    return oldItem.id == newItem.id
                }

            }
        }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
        }
    }

    override fun onViewAttachedToWindow(holder: MovieViewHolder) {
        super.onViewAttachedToWindow(holder)
        getItem(holder.bindingAdapterPosition)?.let { movie ->
            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("movieId", movie.id)
                it.findNavController().navigate(R.id.action_global_to_movie_detail_graph, bundle)
            }
            holder.itemView.setOnLongClickListener {
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                it.findNavController().navigate(R.id.action_global_movieModalFragment, bundle)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: MovieViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, picasso)
    }
}