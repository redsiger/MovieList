package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielist.Screens.alarms.Alarm
import com.example.movielist.data.AlarmsDao
import com.example.movielist.databinding.FragmentMovieModalBinding
import com.example.movielist.foundation.BaseModalFragment
import com.example.movielist.foundation.BaseMovieItem
import com.example.movielist.utils.showDatePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieModalFragment: BaseModalFragment() {

    private var _binding: FragmentMovieModalBinding? = null
    private val mBinding get() =  _binding!!
    private val args: MovieModalFragmentArgs by navArgs()
    private val movie: BaseMovieItem by lazy {
        args.movie
    }
    @Inject lateinit var alarmsDao: AlarmsDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieModalBinding.inflate(inflater, container, false)
        mBinding.modalTitle.text = movie.title

        mBinding.movieDetailRemindBtn.setOnClickListener {
            showDatePicker { time ->
                val alarm = Alarm(movie.id, movie.title, time)
                lifecycleScope.launch {
                    alarmsDao.addAlarm(alarm)
                }
                findNavController().navigateUp()
            }
        }

        return mBinding.root
    }


    companion object {
        val MODAL_TAG = "$this + MODAL_TAG"
    }
}