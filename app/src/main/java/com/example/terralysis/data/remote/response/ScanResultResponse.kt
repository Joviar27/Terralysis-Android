package com.example.terralysis.data.remote.response

import com.google.gson.annotations.SerializedName

data class ScanResultResponse(

	@field:SerializedName("image")
	val image: Image,

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class Image(

	@field:SerializedName("imageId")
	val imageId: String,

	@field:SerializedName("originalname")
	val originalname: String,

	@field:SerializedName("ciri_fisik")
	val ciriFisik: String,

	@field:SerializedName("ciri_morfologi")
	val ciriMorfologi: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("kandungan")
	val kandungan: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("long_desc")
	val longDesc: String,

	@field:SerializedName("persebaran")
	val persebaran: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("kelas")
	val kelas: String,

	@field:SerializedName("mimetype")
	val mimetype: String,

	@field:SerializedName("short_desc")
	val shortDesc: String,

	@field:SerializedName("ciri_kimia")
	val ciriKimia: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
