package com.example.terralysis.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("image")
	val image: List<ImageItem>? = null,

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class ImageItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("imageId")
	val imageId: String,

	@field:SerializedName("kelas")
	val kelas: String,

	@field:SerializedName("url")
	val url: String
)
