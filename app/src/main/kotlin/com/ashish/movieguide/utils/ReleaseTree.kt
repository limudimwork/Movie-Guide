package com.ashish.movieguide.utils

import android.util.Log
import timber.log.Timber

/**
 * A [Timber.Tree] for release builds. This will log error and warning messages only.
 */
class ReleaseTree : Timber.DebugTree() {

    override fun isLoggable(tag: String?, priority: Int) = priority >= Log.ERROR
}