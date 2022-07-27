package com.example.price

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class Dolar(
    val id: String,
    val tipo: String,
    val fecha: String,
    val venta: Double,
    val compra: Double
)

data class Dolares(
    @SerializedName("message") val dolares: MutableList<Dolar>
)

data class RequestBody(
    var tipo: String,
    var fecha: String,
    var venta: Double,
    var compra: Double
)