package com.example.movielist.screens.movieDetail.credits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.squareup.picasso.Picasso

class CrewAdapter(private val mPicasso: Picasso): RecyclerView.Adapter<CrewViewHolder>() {

    private var crewList: List<Crew> = emptyList()
    fun setList(list: List<Crew>) {
        val diffUtilCallBack = CrewAdapterDiffUtil(crewList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        crewList = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit, parent, false)
        return CrewViewHolder(view, mPicasso)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val CrewMember = crewList[position]
        holder.bind(CrewMember)
    }

    override fun getItemCount(): Int = crewList.size
}

class CrewAdapterDiffUtil(
    private val oldList: List<Crew>,
    private val newList: List<Crew>
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
