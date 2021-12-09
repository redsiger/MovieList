package com.example.movielist.screens.reminders.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.databinding.ItemReminderBinding
import com.example.movielist.di.LOCALE
import com.example.movielist.screens.reminders.data.Reminder
import java.text.SimpleDateFormat
import java.util.*

class AlarmsAdapter: RecyclerView.Adapter<AlarmViewHolder>() {

    private var remindersList: MutableList<Reminder> = mutableListOf()

    fun setList(list: List<Reminder>) {
        val mutableList = list.toMutableList()
        val diffUtilCallBack = RemindersAdapterDiffUtil(remindersList, mutableList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        remindersList = mutableList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val reminder: Reminder = remindersList[position]
        holder.bind(reminder)
    }

    override fun onViewAttachedToWindow(holder: AlarmViewHolder) {
        super.onViewAttachedToWindow(holder)
        val reminder = remindersList[holder.bindingAdapterPosition]
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("reminder", reminder)
            it.findNavController().navigate(R.id.action_global_reminderModalFragment, bundle)
        }
    }

    override fun onViewDetachedFromWindow(holder: AlarmViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int = remindersList.size

}

class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = ItemReminderBinding.bind(itemView)
    val dateFormatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm", LOCALE)
    fun bind(reminder: Reminder) {
        with(binding) {
            alarmTitle.text = reminder.movieTitle
            alarmTime.text = dateFormatter.format(Date(reminder.time))
        }
    }
}

class RemindersAdapterDiffUtil(
    private val oldList: List<Reminder>,
    private val newList: List<Reminder>
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