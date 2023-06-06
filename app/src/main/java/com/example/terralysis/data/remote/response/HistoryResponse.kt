package com.example.terralysis.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("imageId")
	val imageId: String,

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("originalname")
	val originalname: String,

	@field:SerializedName("mimetype")
	val mimetype: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
