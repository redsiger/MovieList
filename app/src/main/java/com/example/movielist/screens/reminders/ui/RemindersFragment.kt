package com.example.movielist.screens.reminders.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.data.reminder.RemindersDao
import com.example.movielist.databinding.FragmentRemindersBinding
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.screens.reminders.data.Reminder
import com.example.movielist.utils.goneView
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import com.example.movielist.utils.showView
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
    @Inject lateinit var alarmDao: RemindersDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRemindersBinding.bind(view)
        initPage()
        mViewModel.getReminders()
    }

    private fun initPage() {
        mBinding.remindersRecycler.adapter = mAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.remindersRecycler.layoutManager = layoutManager
        setStateObserver()
    }

    private fun renderAlarms(reminders: List<Reminder>) {
        if (reminders.isEmpty()) mBinding.remindersNoAlarmsYetText.showView()
        else mBinding.remindersNoAlarmsYetText.goneView()
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
        onTryAgain(mBinding.root) { mViewModel.getReminders() }
    }
}