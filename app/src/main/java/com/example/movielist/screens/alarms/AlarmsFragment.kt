package com.example.movielist.screens.alarms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.data.RepositoryListener
import com.example.movielist.data.alarm.AlarmsDao
import com.example.movielist.databinding.FragmentAlarmsBinding
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmsFragment: BaseFragment(R.layout.fragment_alarms) {

    private var _binding: FragmentAlarmsBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: AlarmsViewModel by activityViewModels()
    private val mAdapter: AlarmsAdapter by lazy {
        AlarmsAdapter()
    }
    @Inject lateinit var alarmDao: AlarmsDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAlarmsBinding.bind(view)
        initPage()
        mViewModel.getAlarms()
    }

    private fun initPage() {
        mBinding.alarmsRecycler.adapter = mAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.alarmsRecycler.layoutManager = layoutManager
        setStateObserver()
    }

    private fun renderAlarms(alarms: List<Alarm>) {
        mAdapter.setList(alarms)
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
//        mViewModel.screenStateExp.observe(
//            viewLifecycleOwner, { status ->
//                renderSimpleResult(
//                    mBinding.root,
//                    status
//                ) {
//                    renderAlarms(it)
//                }
//            }
//        )
        onTryAgain(mBinding.root) { mViewModel.getAlarms() }
    }
}