package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json

data class ImageItem(
        val id: Long? = null,
        @Json(name = "file_path") val filePath: String? = null
)