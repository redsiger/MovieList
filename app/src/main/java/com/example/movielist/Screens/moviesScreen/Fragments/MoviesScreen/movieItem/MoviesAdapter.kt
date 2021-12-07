package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.movieItem.MovieViewHolder
import com.example.movielist.foundation.BaseMovieItem
import com.example.movielist.foundation.RecyclerItemWidthListener
import com.squareup.picasso.Picasso

/**
 * Adapter responsible for populate RecyclerView by MovieItems
 */
class MovieAdapter(
    private val mPicasso: Picasso,
    private val itemWidthListener: RecyclerItemWidthListener?
    ) : RecyclerView.Adapter<MovieViewHolder>() {

    constructor(mPicasso: Picasso): this(mPicasso, null)

    var movieList: MutableList<BaseMovieItem> = mutableListOf()

    fun setList(list: List<BaseMovieItem>) {
        val diffUtilCallBack = MovieListComparator(movieList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        movieList = list.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie = movie)
    }

    override fun onViewAttachedToWindow(holder: MovieViewHolder) {
        super.onViewAttachedToWindow(holder)
        val movie = movieList[holder.bindingAdapterPosition]
        holder.itemView.setOnClickListener {
            val bundle = bundleOf("movieId" to movie.id)
            it.findNavController().navigate(R.id.action_global_to_movie_detail_graph, bundle)
        }
        holder.itemView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            it.findNavController().navigate(R.id.action_global_movieModalFragment, bundle)
            return@setOnLongClickListener true
        }
    }

    override fun onViewDetachedFromWindow(holder: MovieViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        val viewHolder = MovieViewHolder(view, mPicasso)

        return viewHolder
    }

    override fun getItemCount(): Int = movieList.size
}

class MovieListComparator(
    private val newList: List<BaseMovieItem>,
    private val oldList: List<BaseMovieItem>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (newItemPosition >= oldList.size) {
            false
        } else {
            val oldItem = oldList[oldItemPosition]
            val newItem = oldList[newItemPosition]
            oldItem.id == newItem.id
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.originalTitle == newItem.originalTitle
    }

}
