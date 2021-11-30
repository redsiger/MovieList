package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.R
import com.example.movielist.databinding.FragmentMoviesScreenBinding
import com.example.movielist.network.paging.MoviePagingAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesPagingScreenFragment : Fragment(R.layout.fragment_movies_screen) {

    @Inject
    lateinit var mPicasso: Picasso
    private var _binding: FragmentMoviesScreenBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MoviesPagingScreenViewModel by viewModels()
    private val mAdapter: MoviePagingAdapter by lazy {
        MoviePagingAdapter(mPicasso)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoviesScreenBinding.bind(view)
        initMovieList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentMoviesScreenBinding.inflate(inflater, container, false)
//        initMovieList()
//        return mBinding.root
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupLayoutManager(binding: FragmentMoviesScreenBinding, recyclerAdapter: MoviePagingAdapter) {
        binding.startScreenPopularsRecycler.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val width = binding.startScreenPopularsRecycler.width
                    Log.e("setSpanCount", "recycler's width = $width")
                    val itemWidth = resources.getDimensionPixelSize(R.dimen.item_movie_img_width)
                    Log.e("setSpanCount", "item's width = $width")
                    if (width > 0 && itemWidth > 0) {
                        binding.startScreenPopularsRecycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val spanCount = width / itemWidth
                        Log.e("setSpanCount", "spanCount = $spanCount")
                        binding.startScreenPopularsRecycler.adapter = recyclerAdapter
                        binding.startScreenPopularsRecycler.layoutManager = GridLayoutManager(requireContext(), spanCount)
                    }
                }
            }
        )
    }

    private fun initMovieList() {
        setupLayoutManager(
            mBinding,
            mAdapter
        )


//        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
//            when (state.refresh) {
//                is LoadState.Loading -> {
//                    mBinding.startScreenProgress.visibility = View.VISIBLE
//                    mBinding.startScreenPopularsRecycler.visibility = View.GONE
//                    mBinding.startScreenRefreshBtn.visibility = View.GONE
//                }
//                is LoadState.Error -> {
//                    mBinding.startScreenProgress.visibility = View.GONE
//                    mBinding.startScreenPopularsRecycler.visibility = View.GONE
//                    mBinding.startScreenRefreshBtn.visibility = View.VISIBLE
//                    Toast.makeText(requireContext(), (state.refresh as LoadState.Error).error.message.toString(), Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    mBinding.startScreenPopularsRecycler.visibility = View.VISIBLE
//                    mBinding.startScreenProgress.visibility = View.GONE
//                    mBinding.startScreenRefreshBtn.visibility = View.GONE
//                }
//            }
//        }
        lifecycleScope.launch {
//            delay(2000)
            receiveMovies()
        }

        initRefresh()
    }

    private suspend fun receiveMovies() {
        mViewModel.movies.collectLatest {
            mAdapter.submitData(it)
        }
    }

    private fun initRefresh() {
//        mBinding.startScreenRefreshBtn.setOnClickListener {
//            mViewModel.isGenresLoaded()
//            mAdapter.refresh()
//        }
    }
}