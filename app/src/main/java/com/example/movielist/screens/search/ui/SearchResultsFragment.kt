package com.example.movielist.screens.search.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.screens.movies.MoviesScreen.MovieAdapter
import com.example.movielist.databinding.FragmentSearchResultsBinding
import com.example.movielist.foundation.BaseMotionFragment
import com.example.movielist.utils.fetchMore
import com.example.movielist.utils.renderSimpleResult
import com.example.movielist.utils.setupGridLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsFragment: BaseMotionFragment(R.layout.fragment_search_results) {

    @Inject
    lateinit var mPicasso: Picasso
    private var _binding: FragmentSearchResultsBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: SearchResultsViewModel by viewModels()
    private val args by navArgs<SearchResultsFragmentArgs>()
    private val searchQuery by lazy {
        args.searchQuery
    }
    private val mAdapter: MovieAdapter by lazy {
        MovieAdapter(mPicasso)
    }
    private val menuActions: (item: MenuItem) -> Boolean by lazy {
        initMenuActions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchResultsBinding.bind(view)
        val toolbar = mBinding.searchResultsToolbar
        setupNavigation(toolbar, searchQuery)
        setupGridLayoutManager(mBinding.searchResultsRecycler, mAdapter, resources.getDimensionPixelSize(R.dimen.item_movie_img_width),
            { initRecycler() })
        initObservers()
        setupToolbarMenu(toolbar, R.menu.menu_search_results, menuActions)
    }


    /**
     * Function responsible for initializing toolbar's menu
     */
    private fun initMenuActions(): (item: MenuItem) -> Boolean {
        return {
            when (it.itemId) {
                R.id.menu_search_item -> {
                    Log.e("MENU ITEM", "$it CLICKED")
                    findNavController().navigate(R.id.action_global_searchFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun initObservers() {
        mViewModel.searchResult.observe(viewLifecycleOwner, { status ->
            renderSimpleResult(
                root = mBinding.root,
                status = status,
                onSuccess = {
//                    mAdapter.setList(it)
                    mAdapter.addToList(it)
                }
            )
        })
    }

    private fun initRecycler() {
        mBinding.searchResultsRecycler.fetchMore {
            Log.e("recyclerViewFetchMore", "YES")
            mViewModel.fetchMore()
        }
    }
}