package com.ashish.movies.ui.base.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Ashish on Dec 28.
 */
abstract class RxPresenter<V : MvpView> {

    private var view: V? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    open fun attachView(view: V) {
        this.view = view
    }

    protected fun getView() = view

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    open fun detachView() {
        view = null
    }

    open fun onDestroy() {
        compositeDisposable.clear()
    }
}