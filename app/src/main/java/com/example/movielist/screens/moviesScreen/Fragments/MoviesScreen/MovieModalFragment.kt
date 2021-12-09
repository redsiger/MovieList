package com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielist.databinding.FragmentMovieModalBinding
import com.example.movielist.foundation.BaseModalFragment
import com.example.movielist.foundation.BaseMovieItem
import com.example.movielist.utils.AppNotificator
import com.example.movielist.utils.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieModalFragment: BaseModalFragment() {

    private var _binding: FragmentMovieModalBinding? = null
    private val mBinding get() =  _binding!!
    private val args: MovieModalFragmentArgs by navArgs()
    private val movie: BaseMovieItem by lazy {
        args.movie
    }
    @Inject lateinit var mAppNotificator: AppNotificator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieModalBinding.inflate(inflater, container, false)
        mBinding.modalTitle.text = movie.title

        mBinding.movieDetailRemindBtn.setOnClickListener {
            showDatePicker { time ->
                lifecycleScope.launch {
                    mAppNotificator.setReminder(
                            movie.id,
                            movie.title,
                            time
                    )
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