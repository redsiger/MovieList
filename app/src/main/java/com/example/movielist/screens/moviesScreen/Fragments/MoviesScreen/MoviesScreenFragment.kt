package com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.example.movielist.R
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.moviesPaging.MoviesPagingAdapter
import com.example.movielist.databinding.FragmentMoviesScreenBinding
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.foundation.BaseMovieItem
import com.example.movielist.foundation.RecyclerItemWidthListener
import com.example.movielist.utils.Status
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import com.example.movielist.utils.setupGridLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MoviesScreenFragment: BaseFragment(R.layout.fragment_movies_screen),
    RecyclerItemWidthListener {

    @Inject
    lateinit var mPicasso: Picasso
    private var _binding: FragmentMoviesScreenBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MoviesScreenViewModel by viewModels()
    private val mAdapter: MovieAdapter by lazy {
        MovieAdapter(mPicasso, this)
    }
    private val mPagingAdapter: MoviesPagingAdapter by lazy {
        MoviesPagingAdapter(mPicasso)
    }

    private var itemMovieWidth = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoviesScreenBinding.bind(view)
        val toolbar = mBinding.startScreenToolbar
        setupNavigation(toolbar, title = getString(R.string.app_name))
        setupToolbarMenu(toolbar, R.menu.movies_menu) {
            when (it.itemId) {
                R.id.menu_search_item -> {
                    Log.e("MENU ITEM", "$it CLICKED")
                    findNavController().navigate(R.id.action_moviesScreenFragment_to_searchFragment)
                    true
                }
                R.id.menu_discover_item -> {
                    Log.e("MENU ITEM", "$it CLICKED")
                    findNavController().navigate(R.id.action_global_discoverModal)
                    true
                }
                else -> {
                    false
                }
            }
        }
        initMovieList()
    }


    private fun initMovieList() {

        renderSimpleResult(mBinding.root, Status.Success(Status.Empty), {})

        setupGridLayoutManager(
            mBinding.startScreenPopularsRecycler,
            mPagingAdapter,
            resources.getDimensionPixelSize(R.dimen.item_movie_img_width)
        )

//        mBinding.startScreenPopularsRecycler.adapter = mPagingAdapter
//        val layoutManager = GridLayoutManager(requireContext(), 3)
//        mBinding.startScreenPopularsRecycler.layoutManager = layoutManager
//        mBinding.startScreenPopularsRecycler.addItemDecoration(OffsetRecyclerDecorator(5, layoutManager))

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.moviesPaging.collectLatest {
                mPagingAdapter.submitData(it as PagingData<BaseMovieItem>)
            }
        }


//        mViewModel.movies.observe(viewLifecycleOwner, { status ->
//            renderSimpleResult(
//                root = mBinding.root,
//                status = status,
//                onSuccess = {
//                    mAdapter.setList(it)
//                } )
//        })

        onTryAgain(mBinding.root, { mViewModel.tryAgain() })
    }

    override fun setItemWidth(itemWidth: Int) {
        itemMovieWidth = itemWidth
        Log.e("TRIGGERRREEDDDD", itemWidth.toString())
    }
}