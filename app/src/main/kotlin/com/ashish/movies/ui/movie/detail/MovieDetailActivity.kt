package com.ashish.movies.ui.movie.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.FullDetailContentActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_MOVIE
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import com.ashish.movies.utils.extensions.*

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailActivity : FullDetailContentActivity<MovieDetail, MovieDetailMvpView, MovieDetailPresenter>(),
        MovieDetailMvpView {

    private val statusText: FontTextView by bindView(R.id.status_text)
    private val budgetText: FontTextView by bindView(R.id.budget_text)
    private val genresText: FontTextView by bindView(R.id.genres_text)
    private val runtimeText: FontTextView by bindView(R.id.runtime_text)
    private val revenueText: FontTextView by bindView(R.id.revenue_text)
    private val taglineText: FontTextView by bindView(R.id.tagline_text)
    private val releaseDateText: FontTextView by bindView(R.id.release_date_text)
    private val similarMoviesViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private var movie: Movie? = null
    private var similarMoviesAdapter: RecyclerViewAdapter<Movie>? = null

    companion object {
        private const val EXTRA_MOVIE = "movie"

        fun createIntent(context: Context, movie: Movie?): Intent {
            return Intent(context, MovieDetailActivity::class.java)
                    .putExtra(EXTRA_MOVIE, movie)
        }
    }

    private val onSimilarMovieItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val movie = similarMoviesAdapter?.getItem<Movie>(position)
            val intent = createIntent(this@MovieDetailActivity, movie)
            startActivityWithTransition(view, R.string.transition_movie_poster, intent)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MovieDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_movie

    override fun getIntentExtras(extras: Bundle?) {
        movie = extras?.getParcelable(EXTRA_MOVIE)
        posterImage.setTransitionName(R.string.transition_movie_poster)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(movie?.id)

    override fun getBackdropPath() = movie?.backdropPath.getBackdropUrl()

    override fun getPosterPath() = movie?.posterPath.getPosterUrl()

    override fun getItemTitle(): String = movie?.title ?: ""

    override fun showDetailContent(detailContent: MovieDetail) {
        detailContent.apply {
            if (getBackdropPath().isNullOrEmpty() && backdropPath.isNotNullOrEmpty()) {
                showBackdropImage(backdropPath.getBackdropUrl())
            }

            setTMDbRating(detailContent.voteAverage)
            this@MovieDetailActivity.imdbId = imdbId
            titleText.setTitleAndYear(title, releaseDate)
            overviewText.text = overview ?: NOT_AVAILABLE
            taglineText.text = tagline ?: NOT_AVAILABLE
            statusText.text = status ?: NOT_AVAILABLE
            budgetText.text = budget.getFormattedNumber()
            revenueText.text = revenue.getFormattedNumber()
            runtimeText.text = runtime.getFormattedRuntime()
            genresText.text = genres.convertListToCommaSeparatedText { it.name.toString() }
            releaseDateText.text = releaseDate.getFormattedReleaseDate(this@MovieDetailActivity)
        }
        super.showDetailContent(detailContent)
    }

    override fun showSimilarMoviesList(similarMoviesList: List<Movie>) {
        similarMoviesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_MOVIE,
                onSimilarMovieItemClickLitener)

        inflateViewStubRecyclerView(similarMoviesViewStub, R.id.similar_content_recycler_view,
                similarMoviesAdapter!!, similarMoviesList)
    }

    override fun performCleanup() {
        similarMoviesAdapter?.removeListener()
        super.performCleanup()
    }
}