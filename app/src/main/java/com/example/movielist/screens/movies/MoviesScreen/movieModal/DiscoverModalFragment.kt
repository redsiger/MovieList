package com.example.movielist.screens.movies.MoviesScreen.movieModal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielist.databinding.FragmentDiscoverBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DiscoverModalFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val mBinding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return mBinding.root
    }
}