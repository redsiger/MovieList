package com.example.movielist.Screens.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.databinding.AlarmItemBinding
import com.example.movielist.di.LOCALE
import java.text.SimpleDateFormat
import java.util.*

class AlarmsAdapter: RecyclerView.Adapter<AlarmViewHolder>() {

    private var alarmsList: MutableList<Alarm> = mutableListOf()

    fun setList(list: List<Alarm>) {
        val mutableList = list.toMutableList()
        val diffUtilCallBack = AlarmsAdapterDiffUtil(alarmsList, mutableList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        alarmsList = mutableList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder( view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm: Alarm = alarmsList[position]
        holder.bind(alarm)
    }

    override fun onViewAttachedToWindow(holder: AlarmViewHolder) {
        super.onViewAttachedToWindow(holder)
        val alarm = alarmsList[holder.bindingAdapterPosition]
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("alarm", alarm)
            it.findNavController().navigate(R.id.action_global_alarmModalFragment, bundle)
        }

    }

    override fun onViewDetachedFromWindow(holder: AlarmViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int = alarmsList.size

}

class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = AlarmItemBinding.bind(itemView)
    val dateFormatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm", LOCALE)
    fun bind(alarm: Alarm) {
        with(binding) {
            alarmTitle.text = alarm.movieTitle
            alarmTime.text = dateFormatter.format(Date(alarm.time))
        }
    }
}

class AlarmsAdapterDiffUtil(
    private val oldList: List<Alarm>,
    private val newList: List<Alarm>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.time == newItem.time
    }

}