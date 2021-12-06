package com.example.movielist.Screens.movieDetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.text.TextUtilsCompat
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
import com.example.movielist.network.Movie
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
//class MovieDetailFragment: BaseMotionFragment() {
class MovieDetailFragment: BaseMotionFragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MovieDetailViewModel by viewModels()
//    private val args by navArgs<MovieDetailFragmentArgs>()
//    private val movieId by lazy {
//        args.movieId
//    }
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

        setupNavigation(mBinding.movieDetailToolbar)
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

        mBinding.movieDetailContentScrolling.movieDetailTrailerImg.setOnClickListener(showTrailerClickListener)
    }

    private fun setRecommendations(movies: List<MovieRecommendation>) {
        if(movies.isEmpty()){
            mBinding.movieDetailContentScrolling.movieDetailRecommendationsContainer.visibility = View.GONE
            return
        }
        with(mBinding.movieDetailContentScrolling.movieDetailRecommendationsRecycler) {
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
        with(mBinding.movieDetailContentScrolling.movieDetailCastRecycler) {
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
        with(mBinding.movieDetailContentScrolling.movieDetailCrewRecycler) {
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
            movieDetailToolbarTitleText.text = movie.title
            movieDetailTitleExpandedText .text = movie.title
            movieDetailTitleExpandedGenresText.text = movie.genres
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
                    .into(movieDetailBackgroundImg)
            } else mPicasso.load(R.drawable.item_movie_placeholder).into(movieDetailBackgroundImg)
        }
    }

    private fun setContentScrolling(movie: MovieById) {
        with(mBinding) {
            with(movieDetailContentScrolling) {

                // Trailer
                if(movie.videos.results.isNotEmpty()) {
                    // Background
                    mPicasso
                        .load(TMDB_IMG_URL + movie.backdropPath)
                        .centerCrop()
                        .fit()
                        .into(movieDetailTrailerImg)

                    // Show trailer
                    setShowTrailerClickListener(movie.videos.results[0].key)
                } else movieDetailTrailerContainer.visibility = View.GONE

                // Rating
                movieDetailRatingText.text = movie.voteAverage.toString()
                movieDetailRatingCountText.text = getString(R.string.movie_detail_votes_count, movie.voteCount.toString())

                // Overview
                if (movie.overview.length > 100) {
                    movieDetailOverviewText.maxLines = 3
                    movieDetailOverviewText.ellipsize = TextUtils.TruncateAt.END
                    movieDetailOverviewText.text = movie.overview
                    movieDetailOverviewText.setOnClickListener {
                        if (movieDetailOverviewText.maxLines < Integer.MAX_VALUE) {
                            movieDetailOverviewText.maxLines = Integer.MAX_VALUE
                        } else {
                            movieDetailOverviewText.maxLines = 3
                        }
                    }
                } else movieDetailOverviewText.text = movie.overview

                // About
                movieDetailAboutCountryText .text = movie.productionCountries.
                joinToString(
                    separator = ", ",
                    transform = { it.name }
                )

                // Original title
                movieDetailAboutOriginalTitleText.text = movie.originalTitle

                // Budget
                movieDetailAboutBudgetText.text = convertBudget(movie.budget)

                // Release Date
//                aboutRelease.text = convertDate(movie.releaseDate)
                movieDetailAboutReleaseText.text = movie.releaseDate

            }
        }
    }

    private fun convertBudget(budget: Int): String {
        val format = NumberFormat.getInstance(LOCALE)
        format.maximumFractionDigits = 0
        Log.e("BUDGET", format.format(budget.toDouble()))
        return format.format(budget.toDouble())
    }
}