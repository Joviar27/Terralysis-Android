package com.example.terralysis.data.remote.response

import com.google.gson.annotations.SerializedName

data class ScanRequestResponse(

	@field:SerializedName("data")
	val data: DataRequest,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataRequest(

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
