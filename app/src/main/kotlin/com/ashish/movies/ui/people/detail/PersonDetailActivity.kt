package com.ashish.movies.ui.people.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.Person
import com.ashish.movies.data.models.PersonDetail
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_MOVIE
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_TV
import com.ashish.movies.utils.ApiConstants.PROFILE_ORIGINAL_URL_PREFIX
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTransitionName

/**
 * Created by Ashish on Jan 04.
 */
class PersonDetailActivity : BaseDetailActivity<PersonDetail, BaseDetailMvpView<PersonDetail>, PersonDetailPresenter>() {

    private val birthdayText: FontTextView by bindView(R.id.birthday_text)
    private val placeOfBirthText: FontTextView by bindView(R.id.place_of_birth_text)

    private var person: Person? = null

    private val onCastItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            onPersonCreditItemClicked(castAdapter, position, view)
        }
    }

    private val onCrewItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            onPersonCreditItemClicked(crewAdapter, position, view)
        }
    }

    private fun onPersonCreditItemClicked(adapter: RecyclerViewAdapter<Credit>?, position: Int, view: View) {
        val credit = adapter?.getItem<Credit>(position)
        val mediaType = credit?.mediaType

        if (MEDIA_TYPE_MOVIE == mediaType) {
            val movie = Movie(credit?.id, credit?.title, posterPath = credit?.posterPath)
            val intent = MovieDetailActivity.createIntent(this@PersonDetailActivity, movie)
            startActivityWithTransition(view, R.string.transition_movie_poster, intent)

        } else if (MEDIA_TYPE_TV == mediaType) {
            val tvShow = TVShow(credit?.id, credit?.name, posterPath = credit?.posterPath)
            val intent = TVShowDetailActivity.createIntent(this@PersonDetailActivity, tvShow)
            startActivityWithTransition(view, R.string.transition_tv_poster, intent)
        }
    }

    companion object {
        const val EXTRA_PERSON = "person"

        fun createIntent(context: Context, person: Person?): Intent {
            return Intent(context, PersonDetailActivity::class.java)
                    .putExtra(EXTRA_PERSON, person)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(PersonDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_person

    override fun getIntentExtras(extras: Bundle?) {
        person = extras?.getParcelable(EXTRA_PERSON)
        posterImage.setTransitionName(R.string.transition_person_profile)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(person?.id)

    override fun getBackdropPath(): String {
        val backdropPath = person?.profilePath
        return if (backdropPath.isNotNullOrEmpty()) PROFILE_ORIGINAL_URL_PREFIX + backdropPath else ""
    }

    override fun getPosterPath() = getBackdropPath()

    override fun showDetailContent(detailContent: PersonDetail?) {
        detailContent?.apply {
            titleText.text = name
            overviewText.text = biography
            placeOfBirthText.applyText(placeOfBirth)
            this@PersonDetailActivity.imdbId = imdbId
            birthdayText.applyText(birthday.getFormattedReleaseDate(this@PersonDetailActivity))
        }
        super.showDetailContent(detailContent)
    }

    override fun getItemTitle() = person?.name ?: ""

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener
}