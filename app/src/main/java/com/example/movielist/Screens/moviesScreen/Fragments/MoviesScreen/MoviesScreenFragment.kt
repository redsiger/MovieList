package com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.R
import com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.movieItem.OffsetRecyclerDecorator
import com.example.movielist.databinding.FragmentMoviesScreenBinding
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.foundation.RecyclerItemWidthListener
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
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

    private var itemMovieWidth = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMoviesScreenBinding.bind(view)
        val toolbar = mBinding.startScreenToolbar
        setupNavigation(toolbar, title = getString(R.string.app_name))
        setupToolbarMenu(toolbar, R.menu.movies_menu, {
            when (it.itemId) {
                R.id.menu_search_item -> {
                    Log.e("MENU ITEM", "$it CLICKED")
                    val bundle = Bundle()
                    val id = 550
                    bundle.putInt("movieId", id)
                    findNavController().navigate(R.id.action_global_to_search)
                    true
                }
                else -> {
                    false
                }
            }
        })
        initMovieList()
    }



    /**
     * Function responsible for setting up a RecyclerView's layoutManager as GridLayoutManager,
     * and setting up layoutManager's spanCount by auto
     */
    private fun setupLayoutManager(binding: FragmentMoviesScreenBinding, recyclerAdapter: MovieAdapter) {

        binding.startScreenPopularsRecycler.adapter = recyclerAdapter
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.startScreenPopularsRecycler.layoutManager = gridLayoutManager

        binding.startScreenPopularsRecycler.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val width = binding.startScreenPopularsRecycler.width
                    Log.e("MEASURINGGGGGGG", itemMovieWidth.toString())
                    val itemWidth = resources.getDimensionPixelSize(R.dimen.item_movie_img_width)
                    if (width > 0 && itemWidth > 0) {
                        Log.e("SUCCESSSSS", "$itemMovieWidth - item, $width - recycler")
                        binding.startScreenPopularsRecycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val spanCount = width / itemWidth
                        binding.startScreenPopularsRecycler.adapter = recyclerAdapter
                        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
                        binding.startScreenPopularsRecycler.layoutManager = gridLayoutManager
                        binding.startScreenPopularsRecycler.addItemDecoration(OffsetRecyclerDecorator(5, gridLayoutManager))
                    }
                }
            }
        )
    }


    private fun initMovieList() {

        setupLayoutManager(mBinding, mAdapter)

        mViewModel.movies.observe(viewLifecycleOwner, { status ->
            renderSimpleResult(
                root = mBinding.root,
                status = status,
                onSuccess = {
                    mAdapter.setList(it)
                } )
        })

        onTryAgain(mBinding.root, { mViewModel.tryAgain() })
    }

    override fun setItemWidth(itemWidth: Int) {
        itemMovieWidth = itemWidth
        Log.e("TRIGGERRREEDDDD", itemWidth.toString())
    }
}