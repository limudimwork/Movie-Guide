package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentView
import com.ashish.movieguide.ui.common.rating.ContentRatingView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailView : FullDetailContentView<MovieDetail>, PersonalContentView,
        ContentRatingView {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}