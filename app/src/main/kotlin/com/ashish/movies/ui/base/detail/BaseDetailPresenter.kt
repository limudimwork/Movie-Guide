package com.ashish.movies.ui.base.detail

import com.ashish.movies.R
import com.ashish.movies.data.models.CreditResults
import com.ashish.movies.data.models.FullDetailContent
import com.ashish.movies.data.models.ImageItem
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.utils.Utils
import com.ashish.movies.utils.extensions.getBackdropUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import io.reactivex.Observable
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * Created by Ashish on Jan 03.
 */
abstract class BaseDetailPresenter<I, V : BaseDetailView<I>> : RxPresenter<V>() {

    protected var contentList: ArrayList<String> = ArrayList()
    protected var fullDetailContent: FullDetailContent<I>? = null

    open fun loadDetailContent(id: Long?) {
        if (fullDetailContent != null) {
            showDetailContent(fullDetailContent!!)
        } else {
            loadFreshData(id)
        }
    }

    private fun loadFreshData(id: Long?) {
        if (Utils.isOnline()) {
            if (id != null) {
                getView()?.showProgress()
                addDisposable(getDetailContent(id)
                        .subscribe({ showDetailContent(it) }, { onLoadDetailError(it, getErrorMessageId()) }))
            }
        } else {
            getView()?.apply {
                showToastMessage(R.string.error_no_internet)
                finishActivity()
            }
        }
    }

    abstract fun getDetailContent(id: Long): Observable<FullDetailContent<I>>

    protected open fun showDetailContent(fullDetailContent: FullDetailContent<I>) {
        getView()?.apply {
            hideProgress()
            this@BaseDetailPresenter.fullDetailContent = fullDetailContent
            addContents(fullDetailContent)
            showDetailContentList(contentList)

            val detailContent = fullDetailContent.detailContent
            if (detailContent != null) {
                showDetailContent(detailContent)

                val imageUrlList = ArrayList<String>()

                val backdropImages = getBackdropImages(detailContent)
                if (backdropImages.isNotNullOrEmpty()) {
                    val backdropImageUrlList = backdropImages!!
                            .map { it.filePath.getBackdropUrl() }
                            .toList()
                    imageUrlList.addAll(backdropImageUrlList)
                }

                val posterImages = getPosterImages(detailContent)
                if (posterImages.isNotNullOrEmpty()) {
                    val posterImageUrlList = posterImages!!
                            .map { it.filePath.getPosterUrl() }
                            .toList()
                    imageUrlList.addAll(posterImageUrlList)
                }

                showCredits(getCredits(detailContent))
                if (imageUrlList.isNotEmpty()) showImageList(imageUrlList)
            }

            val omdbDetail = fullDetailContent.omdbDetail
            if (omdbDetail != null) showOMDbDetail(omdbDetail)
        }
    }

    abstract fun addContents(fullDetailContent: FullDetailContent<I>)

    abstract fun getBackdropImages(detailContent: I): List<ImageItem>?

    abstract fun getPosterImages(detailContent: I): List<ImageItem>?

    abstract fun getCredits(detailContent: I): CreditResults?

    protected fun showCredits(creditResults: CreditResults?) {
        getView()?.apply {
            showItemList(creditResults?.cast) { showCastList(it) }
            showItemList(creditResults?.crew) { showCrewList(it) }
        }
    }

    protected fun onLoadDetailError(t: Throwable, messageId: Int) {
        Timber.e(t)
        getView()?.apply {
            showErrorToast(t, messageId)
            finishActivity()
        }
    }

    abstract fun getErrorMessageId(): Int

    protected fun showErrorToast(t: Throwable, messageId: Int) {
        getView()?.apply {
            if (t is IOException) {
                showToastMessage(R.string.error_no_internet)
            } else {
                showToastMessage(messageId)
            }
        }
    }

    protected fun <T> showItemList(itemList: List<T>?, showData: (List<T>) -> Unit) {
        if (itemList != null && itemList.isNotEmpty()) showData(itemList)
    }
}