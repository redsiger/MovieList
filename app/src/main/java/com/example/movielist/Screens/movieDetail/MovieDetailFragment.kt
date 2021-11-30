package com.example.movielist.Screens.movieDetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.Screens.movieDetail.credits.Cast
import com.example.movielist.Screens.movieDetail.credits.Crew
import com.example.movielist.Screens.movieDetail.credits.CrewAdapter
import com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.MovieAdapter
import com.example.movielist.Screens.moviesScreen.Fragments.MoviesScreen.movieItem.OffsetRecyclerDecorator
import com.example.movielist.databinding.FragmentMovieDetailBinding
import com.example.movielist.di.LOCALE
import com.example.movielist.di.TMDB_IMG_URL
import com.example.movielist.foundation.BaseFragment
import com.example.movielist.foundation.BaseMotionFragment
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment: BaseMotionFragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MovieDetailViewModel by viewModels()
    private val args by navArgs<MovieDetailFragmentArgs>()
    private val movieId by lazy {
        args.movieId
    }
    @Inject lateinit var mPicasso: Picasso
    private val castAdapter: CastAdapter by lazy {
        CastAdapter(mPicasso)
    }
    private val crewAdapter: CrewAdapter by lazy {
        CrewAdapter(mPicasso)
    }
    private val recommendationsAdapter: MovieAdapter by lazy {
        MovieAdapter(mPicasso)
    }

    override var _transitionState: Bundle? = null
    override var _motionLayout: MotionLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMovieDetailBinding.bind(view)
        _motionLayout = mBinding.root
        super.onViewCreated(view, savedInstanceState)
        _transitionState = _motionLayout!!.transitionState

        setupNavigation(mBinding.toolbar)
        setStateObserver()
        onTryAgain(mBinding.root, { mViewModel.tryAgain() })
    }

    private fun setStateObserver() {
        mViewModel.screenState.observe(
            viewLifecycleOwner, { status ->
                renderSimpleResult(
                    mBinding.root,
                    status
                ) {
                    renderMovieDetail(it)
                }
            })
    }

    private fun renderMovieDetail(state: MovieDetailViewModel.MovieDetailState) {
        setToolbarContent(state.movie)
        setContentScrolling(state.movie)
        setCast(state.castList)
        setCrew(state.crewList)
        setRecommendations(state.recommendationsList)
    }

    private fun setShowTrailerClickListener(url: String) {

        val showTrailerClickListener = View.OnClickListener {
            val appSrc: String = "vnd.youtube:" + url
            val webSrc: String = "http://www.youtube.com/watch?v=" + url
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appSrc))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webSrc))

            try {
                requireContext().startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                requireContext().startActivity(webIntent)
            }
        }

        mBinding.contentScrolling.trailer.setOnClickListener(showTrailerClickListener)
    }

    private fun setRecommendations(movies: List<MovieRecommendation>) {
        if(movies.isEmpty()){
            mBinding.contentScrolling.recommendationsContainer.visibility = View.GONE
            return
        }
        with(mBinding.contentScrolling.recommendationsRecycler) {
            adapter = recommendationsAdapter
            val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                linearLayoutManager))
        }
        recommendationsAdapter.setList(movies)
    }

    private fun setCast(cast: List<Cast>) {
        with(mBinding.contentScrolling.castRecycler) {
            adapter = castAdapter
            val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                linearLayoutManager))
        }
        castAdapter.setList(cast)
    }

    private fun setCrew(crew: List<Crew>) {
        with(mBinding.contentScrolling.crewRecycler) {
            adapter = crewAdapter
            val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                linearLayoutManager))
        }
        crewAdapter.setList(crew)
    }

    private fun setToolbarContent(movie: MovieById) {
        with(mBinding) {
            // CollapsingToolbar
            title.text = movie.title
            titleExpanded.text = movie.title
            titleExpandedGenres.text = movie.genres
                .take(3)
                .joinToString(
                    separator = ", ",
                    transform = { it.name }
                )

            // CollapsingToolbar Background
            if (movie.backdropPath != "null") {
                mPicasso
                    .load(TMDB_IMG_URL + movie.backdropPath)
                    .centerCrop()
                    .fit()
                    .into(backgroundImg)
            } else mPicasso.load(R.drawable.item_movie_placeholder).into(backgroundImg)
        }
    }

    private fun setContentScrolling(movie: MovieById) {
        with(mBinding) {
            with(contentScrolling) {

                // Trailer
                if(movie.videos.results.isNotEmpty()) {
                    // Background
                    mPicasso
                        .load(TMDB_IMG_URL + movie.backdropPath)
                        .centerCrop()
                        .fit()
                        .into(trailer)

                    // Show trailer
                    setShowTrailerClickListener(movie.videos.results[0].key)
                } else trailerContainer.visibility = View.GONE

                // Rating
                rating.text = movie.voteAverage.toString()
                ratingCount.text = getString(R.string.movie_detail_votes_count, movie.voteCount.toString())

                // Overview
                overview.text = movie.overview

                // About
                aboutCountry.text = movie.productionCountries.
                joinToString(
                    separator = ", ",
                    transform = { it.name }
                )

                // Original title
                aboutOriginalTitle.text = movie.originalTitle

                // Budget
                aboutBudget.text = convertBudget(movie.budget)

                // Release Date
//                aboutRelease.text = convertDate(movie.releaseDate)
                aboutRelease.text = movie.releaseDate

            }
        }
    }

    private fun convertBudget(budget: Int): String {
        val format = NumberFormat.getInstance(LOCALE)
        format.maximumFractionDigits = 0
        Log.e("BUDGET", format.format(budget.toDouble()))
        return format.format(budget.toDouble())
    }

    private fun convertDate(dateToParse: String): String {
        val format = "yyyy-MM-dd"
        Log.e("RELEASE DATE", dateToParse)
        val dateParsed = SimpleDateFormat(format, LOCALE).parse(format)
        return SimpleDateFormat("dd mmm yyyy").format(dateParsed)
    }
}