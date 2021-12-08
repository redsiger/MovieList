package com.example.movielist.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.MovieAdapter
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.MovieModalFragment
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.movieItem.OffsetRecyclerDecorator
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.moviesPaging.MoviesPagingAdapter
import com.example.movielist.databinding.PartErrorAndLoadingBinding
import com.example.movielist.foundation.BaseFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


/**
 *  Function, by invoking renderResult function, hide all elements on screen,
 *  and depend on Status value shows:
 *      - Progress Bar
 *      - Error message with try again btn
 *      - Screen with some data
 */
fun <T> BaseFragment.renderSimpleResult(
    root: ViewGroup,
    status: Status<T>,
    onSuccess: (T) -> Unit
    ) {

    val binding = PartErrorAndLoadingBinding.bind(root)
    renderResult(
        root = root,
        status = status,
        onLoading = {
            binding.progressBar.visibility = View.VISIBLE
            Log.e("PROGRESSBAR", "VISIBLE")
        },
        onError = { exception ->  
            binding.errorMessage.text = exception.message.toString()
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { resultData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach {
                    it.visibility = View.VISIBLE
                    Log.e("VIEW NAME VISIBLE", resources.getResourceEntryName(it.id).toString())
                }

            onSuccess(resultData)
        }
    )
}

/**
 *  Function responsible for fetching data again in case of error
 */
fun BaseFragment.onTryAgain(
    root: ViewGroup,
    onTryAgainPressed: () -> Unit
) {
    val binding = PartErrorAndLoadingBinding.bind(root)
    binding.btnTryAgain.setOnClickListener {
        onTryAgainPressed()
        Log.e("TryAgain", "TryAgain pressed")
    }
}

/**
 *  Function adding GridLayoutManager to RecyclerView.
 *  Span count is setting by auto depend on itemWidth
 */
fun BaseFragment.setupGridLayoutManager(recyclerView: RecyclerView, recyclerAdapter: MoviesPagingAdapter, itemWidth: Int) {

    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = recyclerView.width
                if (width > 0 && itemWidth > 0) {
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val spanCount = width / itemWidth
                    recyclerView.adapter = recyclerAdapter
                    val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.addItemDecoration(OffsetRecyclerDecorator(5, gridLayoutManager))
                }
            }
        }
    )
}

fun BaseFragment.setupGridLayoutManager(recyclerView: RecyclerView, recyclerAdapter: MovieAdapter, itemWidth: Int) {

    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = recyclerView.width
                if (width > 0 && itemWidth > 0) {
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val spanCount = width / itemWidth
                    recyclerView.adapter = recyclerAdapter
                    val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.addItemDecoration(OffsetRecyclerDecorator(5, gridLayoutManager))
                }
            }
        }
    )
}

fun Fragment.showDatePicker(action: (time: Long) -> Unit, calendar: Calendar, time: Long) {
    Log.e("CALENDAR", calendar.time.toString())
    val constraints =
        CalendarConstraints.Builder()
            .setStart(time)
            .setValidator(DateValidatorPointForward.now())
            .build()

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("R.string.datePicker")
        .setSelection(time)
        .setCalendarConstraints(constraints)
        .build()

    datePicker.addOnPositiveButtonClickListener { timePicked ->
        getTimePicker(timePicked, calendar, action)
            .show(childFragmentManager, MovieModalFragment.MODAL_TAG)

    }

    datePicker.show(childFragmentManager, MovieModalFragment.MODAL_TAG)
    Log.e("DATE_PICKER", "3")
}

fun Fragment.showDatePicker(time: Long, action: (time: Long) -> Unit) {
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    Log.e("DATE_PICKER_time_before", "${calendar.timeInMillis}")
    Log.e("DATE_PICKER_time_before", "$calendar")
    calendar.timeInMillis = time
    Log.e("DATE_PICKER_time_after", "${calendar.timeInMillis}")
    Log.e("DATE_PICKER_time_after", "$calendar")
    Log.e("DATE_PICKER", "2")
    showDatePicker(action, calendar, time)
}

/**
 * Takes lambda that is executed after date selecting
 */
fun Fragment.showDatePicker(action: (time: Long) -> Unit) {
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    val today = calendar.timeInMillis
    Log.e("DATE_PICKER", "1")
    showDatePicker(action, calendar, today)
}

fun Fragment.getTimePicker(time: Long, calendar: Calendar, action: (time: Long) -> Unit): MaterialTimePicker {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
        .setMinute(calendar.get(Calendar.MINUTE))
        .setTitleText("R.string.timePicker")
        .build()

    calendar.timeInMillis = time

    timePicker.addOnPositiveButtonClickListener {
        Log.e("DatePickerC", calendar.toString())
        setHours(calendar, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        Log.e("DatePickerD", calendar.toString())
        Log.e("DatePickerD", calendar.timeInMillis.toString())
        action(calendar.timeInMillis)
//            setAlarm(calendar.timeInMillis, movie)
    }

    return timePicker
}

fun Fragment.setHours(calendar: Calendar, hours: Int) {
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    if (hours > 12) {
        calendar.set(Calendar.HOUR, hours)
        calendar.set(Calendar.AM_PM, 0)
    }
}