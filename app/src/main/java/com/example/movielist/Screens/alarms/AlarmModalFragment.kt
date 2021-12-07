package com.example.movielist.Screens.alarms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielist.databinding.FragmentAlarmModalBinding
import com.example.movielist.di.LOCALE
import com.example.movielist.foundation.BaseModalFragment
import com.example.movielist.network.movie.Movie
import com.example.movielist.utils.Status
import com.example.movielist.utils.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AlarmModalFragment: BaseModalFragment() {

    private var _binding: FragmentAlarmModalBinding? = null
    private val mBinding get() =  _binding!!
    private val mViewModel: AlarmsViewModel by activityViewModels()
    private val args: AlarmModalFragmentArgs by navArgs()
    private val alarm: Alarm by lazy {
        args.alarm
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmModalBinding.inflate(inflater, container, false)

//        mBinding.alarmModalTitleText.text = alarm.movieTitle
//        initBtnRemind()
//        initBtnDelete()

        initStateObserver()
        return mBinding.root
    }

    private fun initStateObserver() {
        mViewModel.modalScreenState.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Status.Success -> {
                    renderModalScreen(status.data)
                }
                else -> {
                    mBinding.alarmModalDeleteBtn.visibility = View.INVISIBLE
                    mBinding.alarmModalRemindBtn.visibility = View.INVISIBLE
                    mBinding.alarmModalLoading.visibility = View.VISIBLE
                }
            }
        })
        mViewModel.getAlarm(alarm.movieId)
    }

    private fun renderModalScreen(data: Alarm) {
        mBinding.alarmModalLoading.visibility = View.GONE
        mBinding.alarmModalDeleteBtn.visibility = View.VISIBLE
        mBinding.alarmModalRemindBtn.visibility = View.VISIBLE
        mBinding.alarmModalTitleText.text = data.movieTitle
        initBtnRemind(data)
        initBtnDelete(data)
    }

    private fun initBtnRemind(alarm: Alarm) {
        val dateFormatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm", LOCALE)
        with(mBinding.alarmModalRemindBtn) {
            text = dateFormatter.format(Date(alarm.time))
            setOnClickListener {
                showDatePicker(alarm.time) { time ->
                    mViewModel.addAlarm(alarm.copy(time = time))
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initBtnDelete(alarm: Alarm) {
        mBinding.alarmModalDeleteBtn.setOnClickListener {
            lifecycleScope.launch {
                mViewModel.deleteAlarm(alarm.movieId)
            }
            findNavController().navigateUp()
        }
    }
}