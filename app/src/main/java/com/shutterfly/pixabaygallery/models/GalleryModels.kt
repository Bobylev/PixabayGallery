package com.shutterfly.pixabaygallery.models

import com.google.gson.annotations.SerializedName

data class GalleryData(
    @SerializedName("hits")
    val galleryItems: List<GalleryItem>
)

data class GalleryItem(
    val id: Int,
    @SerializedName("previewURL")
    val previewUrl: String,
    @SerializedName("largeImageURL")
    val imageURL: String,
    @SerializedName("likes")
    val likes: String
)