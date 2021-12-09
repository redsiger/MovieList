package com.example.movielist.screens.reminders.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.data.alarm.AlarmsDao
import com.example.movielist.databinding.FragmentRemindersBinding
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RemindersFragment: BaseFragment(R.layout.fragment_reminders) {

    private var _binding: FragmentRemindersBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: AlarmsViewModel by activityViewModels()
    private val mAdapter: AlarmsAdapter by lazy {
        AlarmsAdapter()
    }
    @Inject lateinit var alarmDao: AlarmsDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRemindersBinding.bind(view)
        initPage()
        mViewModel.getAlarms()
    }

    private fun initPage() {
        mBinding.alarmsRecycler.adapter = mAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.alarmsRecycler.layoutManager = layoutManager
        setStateObserver()
    }

    private fun renderAlarms(reminders: List<Reminder>) {
        if (reminders.isEmpty()) mBinding.alarmsNoAlarmsYetText.visibility = View.VISIBLE
        else mBinding.alarmsNoAlarmsYetText.visibility = View.GONE
        mAdapter.setList(reminders)
    }

    private fun setStateObserver() {
        mViewModel.screenState.observe(
            viewLifecycleOwner, { status ->
                renderSimpleResult(
                    mBinding.root,
                    status
                ) {
                    renderAlarms(it)
                }
            }
        )
        onTryAgain(mBinding.root) { mViewModel.getAlarms() }
    }
}