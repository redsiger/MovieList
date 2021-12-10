package com.example.movielist.screens.movieDetail.credits.recommedations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.screens.movies.MoviesScreen.movieItem.MovieViewHolder
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.squareup.picasso.Picasso

class RecommendationsAdapter(
    private val mPicasso: Picasso
) : RecyclerView.Adapter<MovieViewHolder>() {

    var movieList: MutableList<MovieRecommendation> = mutableListOf()

    fun setList(list: List<MovieRecommendation>) {
        val diffUtilCallBack = MovieListComparator(movieList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        movieList = list.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie = movie)

        val bundle = Bundle()
        val id = movie.id
        bundle.putInt("movieId", id)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_moviesScreenFragment_to_movie_detail_graph, bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, mPicasso)
    }

    override fun getItemCount(): Int = movieList.size
}

class MovieListComparator(
    private val newList: List<MovieRecommendation>,
    private val oldList: List<MovieRecommendation>
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