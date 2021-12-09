package com.example.movielist.screens.reminders.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielist.databinding.FragmentReminderModalBinding
import com.example.movielist.di.LOCALE
import com.example.movielist.foundation.BaseModalFragment
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.utils.Status
import com.example.movielist.utils.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReminderModalFragment: BaseModalFragment() {

    private var _binding: FragmentReminderModalBinding? = null
    private val mBinding get() =  _binding!!
    private val mViewModel: AlarmsViewModel by activityViewModels()
    private val args: ReminderModalFragmentArgs by navArgs()
    private val reminder: Reminder by lazy {
        args.reminder
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderModalBinding.inflate(inflater, container, false)

        initStateObserver()
        return mBinding.root
    }

    private fun initStateObserver() {
        mViewModel.modalScreenState.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Status.Success -> {
                    renderModalScreen(status.data)
                }
                is Status.Error -> {
                    showError()
                    Log.e("AlarmModalErrorMessage", status.exception.message.toString())
                    val errorMessage = status.exception.message ?: "oops, something went wrong..."
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                else -> {
                    mBinding.alarmModalDeleteBtn.visibility = View.INVISIBLE
                    mBinding.alarmModalRemindBtn.visibility = View.INVISIBLE
                    mBinding.alarmModalLoading.visibility = View.VISIBLE
                }
            }
        })
        mViewModel.getAlarm(reminder.movieId)
    }

    private fun showError() {
        mBinding.alarmModalLoading.visibility = View.GONE
        mBinding.alarmModalDeleteBtn.visibility = View.INVISIBLE
        mBinding.alarmModalRemindBtn.visibility = View.INVISIBLE
        mBinding.alarmModalTitleText.visibility = View.INVISIBLE
    }

    private fun renderModalScreen(data: Reminder) {
        mBinding.alarmModalLoading.visibility = View.GONE
        mBinding.alarmModalDeleteBtn.visibility = View.VISIBLE
        mBinding.alarmModalRemindBtn.visibility = View.VISIBLE
        mBinding.alarmModalTitleText.text = data.movieTitle
        initBtnRemind(data)
        initBtnDelete(data)
    }

    private fun initBtnRemind(reminder: Reminder) {
        val dateFormatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm", LOCALE)
        with(mBinding.alarmModalRemindBtn) {
            text = dateFormatter.format(Date(reminder.time))
            setOnClickListener {
                showDatePicker(reminder.time) { time ->
                    lifecycleScope.launch {
                        mViewModel.setReminder(reminder.movieId, reminder.movieTitle, time)
                    }
                }
            }
        }
    }

    private fun initBtnDelete(reminder: Reminder) {
        mBinding.alarmModalDeleteBtn.setOnClickListener {
            lifecycleScope.launch {
                mViewModel.unsetReminder(reminder.movieId, reminder.movieTitle)
            }
            findNavController().navigateUp()
        }
    }
}