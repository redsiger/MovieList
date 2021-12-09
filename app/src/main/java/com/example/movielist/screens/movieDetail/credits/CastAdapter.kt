package com.example.movielist.screens.movieDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.screens.movieDetail.credits.Cast
import com.example.movielist.screens.movieDetail.credits.CastViewHolder
import com.squareup.picasso.Picasso

class CastAdapter(private val mPicasso: Picasso): RecyclerView.Adapter<CastViewHolder>() {

    private var castList: List<Cast> = emptyList()
    fun setList(list: List<Cast>) {
        val diffUtilCallBack = CastAdapterDiffUtil(castList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        castList = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        return CastViewHolder(view, mPicasso)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.bind(castMember)
    }

    override fun getItemCount(): Int = castList.size
}

class CastAdapterDiffUtil(
    private val oldList: List<Cast>,
    private val newList: List<Cast>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.originalName == newItem.originalName
    }
}