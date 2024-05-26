package com.example.geostatapplication.api.response

import com.google.gson.annotations.SerializedName


data class GeoStatResponse(

	@field:SerializedName("features")
	val features: List<FeaturesItem?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class Properties(

	@field:SerializedName("fid")
	val fid: Int? = null,

	@field:SerializedName("kdkab")
	val kdkab: String? = null,

	@field:SerializedName("nmprov")
	val nmprov: String? = null,

	@field:SerializedName("kdkec")
	val kdkec: String? = null,

	@field:SerializedName("gid")
	val gid: Int? = null,

	@field:SerializedName("sbs")
	val sbs: String? = null,

	@field:SerializedName("sumber")
	val sumber: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("luas")
	val luas: Double? = null,

	@field:SerializedName("kdprov")
	val kdprov: Int? = null,

	@field:SerializedName("tingkat")
	val tingkat: Int? = null,

	@field:SerializedName("periode")
	val periode: String? = null,

	@field:SerializedName("kdsls")
	val kdsls: String? = null,

	@field:SerializedName("muatan")
	val muatan: Int? = null,

	@field:SerializedName("kdbs")
	val kdbs: String? = null,

	@field:SerializedName("nmsls")
	val nmsls: String? = null,

	@field:SerializedName("posisi")
	val posisi: String? = null,

	@field:SerializedName("idbs")
	val idbs: String? = null,

	@field:SerializedName("dominan")
	val dominan: Int? = null,

	@field:SerializedName("khusus")
	val khusus: String? = null,

	@field:SerializedName("kk")
	val kk: Int? = null,

	@field:SerializedName("nmdesa")
	val nmdesa: String? = null,

	@field:SerializedName("bsbtt")
	val bsbtt: Int? = null,

	@field:SerializedName("nmkec")
	val nmkec: String? = null,

	@field:SerializedName("nmkab")
	val nmkab: String? = null,

	@field:SerializedName("kddesa")
	val kddesa: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("bstt_k")
	val bsttK: Int? = null,

	@field:SerializedName("dom_sls")
	val domSls: String? = null,

	@field:SerializedName("base")
	val base: String? = null,

	@field:SerializedName("bstt")
	val bstt: Int? = null
)


data class FeaturesItem(

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("properties")
	val properties: Properties? = null
)


data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<List<List<Any>>>? = null,

	@field:SerializedName("type")
	val type: String? = null
)
