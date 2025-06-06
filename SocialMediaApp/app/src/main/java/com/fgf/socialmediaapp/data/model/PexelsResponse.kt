package com.fgf.socialmediaapp.data.model

import com.google.gson.annotations.SerializedName

data class PexelsResponse(
    @SerializedName("photos") val photos: List<PexelsPhoto>
)

data class PexelsPhoto(
    val id: Int,
    @SerializedName("photographer") val title: String,
    @SerializedName("alt") val body: String,
    @SerializedName("src") val src: PhotoSrc,
    @SerializedName("liked") val liked: Boolean
)

data class PhotoSrc(
    @SerializedName("medium") val medium: String
)

