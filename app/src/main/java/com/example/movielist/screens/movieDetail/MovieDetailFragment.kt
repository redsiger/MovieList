package com.example.movielist.screens.movieDetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielist.R
import com.example.movielist.screens.movieDetail.credits.Cast
import com.example.movielist.screens.movieDetail.credits.Crew
import com.example.movielist.screens.movieDetail.credits.CrewAdapter
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.MovieAdapter
import com.example.movielist.databinding.FragmentMovieDetailBinding
import com.example.movielist.di.LOCALE
import com.example.movielist.di.TMDB_IMG_URL
import com.example.movielist.foundation.BaseMotionFragment
import com.example.movielist.network.MovieById.MovieById
import com.example.movielist.network.recommentadions.MovieRecommendation
import com.example.movielist.screens.moviesScreen.Fragments.MoviesScreen.movieItem.OffsetRecyclerDecorator
import com.example.movielist.utils.onTryAgain
import com.example.movielist.utils.renderSimpleResult
import com.example.movielist.utils.setTruncableText
import com.example.movielist.utils.showDatePicker
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
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
    private val mCastAdapter: CastAdapter by lazy { CastAdapter(mPicasso) }
    private val mCrewAdapter: CrewAdapter by lazy { CrewAdapter(mPicasso) }
    private val mRecommendationsAdapter: MovieAdapter by lazy { MovieAdapter(mPicasso) }
//    private val mLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    override var _transitionState: Bundle? = null
    override var _motionLayout: MotionLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMovieDetailBinding.bind(view)
        _motionLayout = mBinding.root
        super.onViewCreated(view, savedInstanceState)
        _transitionState = _motionLayout!!.transitionState

        setupNavigation(mBinding.movieDetailToolbar)
        initFragment()
        onTryAgain(mBinding.root, { mViewModel.tryAgain() })

    }

    private fun initFragment() {
        initRecyclers()

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

    private fun initRecyclers() {
        initCastRecycler()
        initCrewRecycler()
        initRecommendationRecycler()
    }

    private fun initCastRecycler() {
        with (mBinding.movieDetailContentScrolling.movieDetailCastRecycler) {
            adapter = mCastAdapter
            val castLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = castLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                castLayoutManager
            ))
        }
    }

    private fun initCrewRecycler() {
        with (mBinding.movieDetailContentScrolling.movieDetailCrewRecycler) {
            adapter = mCrewAdapter
            val crewLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = crewLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                crewLayoutManager
            ))
        }
    }

    private fun initRecommendationRecycler() {
        with (mBinding.movieDetailContentScrolling.movieDetailRecommendationsRecycler) {
            adapter = mRecommendationsAdapter
            val recommendationLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager = recommendationLayoutManager
            addItemDecoration(OffsetRecyclerDecorator(
                marginTop = 0,
                marginBottom = 0,
                marginLeft = 5,
                marginRight = 5,
                recommendationLayoutManager
            ))
        }
    }

    private fun renderMovieDetail(state: MovieDetailViewModel.MovieDetailState) {
        setRemindBtn(state.movie, state.alarmIsSet)
        setToolbarContent(state.movie)
        setContentScrolling(state.movie)
        setCast(state.castList)
        setCrew(state.crewList)
        setRecommendations(state.recommendationsList)
    }

    private fun setRemindBtn(movie: MovieById, alarmIsSet: Boolean) {
        if (alarmIsSet) {
            mBinding.movieDetailRemindBtn.text = resources.getString(R.string.btn_delete_reminder_text)
            mBinding.movieDetailRemindBtn.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)
            mBinding.movieDetailRemindBtn.setOnClickListener {
                mViewModel.unsetNotification(movie.id, movie.title)
            }
        } else {
            mBinding.movieDetailRemindBtn.text = resources.getString(R.string.btn_remind_me_later_text)
            mBinding.movieDetailRemindBtn.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_alarm, null)
            mBinding.movieDetailRemindBtn.setOnClickListener {
                showDatePicker { time ->
                    mViewModel.setNotification(movie.id, movie.title, time)
                }
            }
        }

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
        mRecommendationsAdapter.setList(movies)
    }

    private fun setCast(cast: List<Cast>) {
        if (cast.isEmpty()) {
            mBinding.movieDetailContentScrolling.movieDetailCastContainer.visibility = View.GONE
            return
        }
        mCastAdapter.setList(cast)
    }

    private fun setCrew(crew: List<Crew>) {
        if (crew.isEmpty()) {
            mBinding.movieDetailContentScrolling.movieDetailCrewRecycler.visibility = View.GONE
            return
        }
        mCrewAdapter.setList(crew)
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
                movieDetailOverviewText.setTruncableText(
                    text = movie.overview,
                    maxLength = 100,
                    maxLines = 3
                )

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