package com.ashish.movies.data.interactors

import com.ashish.movies.data.api.TVShowService
import com.ashish.movies.data.models.Results
import com.ashish.movies.data.models.TVShow
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 29.
 */
@Singleton
class TVShowInteractor @Inject constructor(val tvShowService: TVShowService) {

    fun getTVShowsByType(tvShowType: String?, page: Int = 1): Observable<Results<TVShow>> {
        return tvShowService.getTVShows(tvShowType, page)
                .observeOn(AndroidSchedulers.mainThread())
    }
}