package com.example.movielist.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.movielist.R
import com.example.movielist.databinding.PartErrorAndLoadingBinding
import com.example.movielist.foundation.BaseFragment


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