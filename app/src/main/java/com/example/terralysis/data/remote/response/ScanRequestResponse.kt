package com.example.terralysis.data.remote.response

import com.google.gson.annotations.SerializedName

data class ScanRequestResponse(

	@field:SerializedName("imageId")
	val imageId: String,

	@field:SerializedName("kelas")
	val kelas: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("url")
	val url: String
)
