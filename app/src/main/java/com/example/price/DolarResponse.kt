package com.example.price

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class DolarResponse (
    @SerializedName("message") var dolares:List<Dolar>

)
data class DolarHistoricoResponse(
    @SerializedName("message") var dolarHistorico:List<DolarHistorico>

)
data class Dolar(
    var nombre: String,
    var venta: Double,
    var compra: Double
)

data class DolarHistorico(
    var fecha: String,
    var venta: Double,
    var compra: Double
)

data class RequestBody(
    var id: Int,
    var nombre: String,
    var venta: Double,
    var compra: Double
)