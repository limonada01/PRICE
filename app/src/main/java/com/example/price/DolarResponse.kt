package com.example.price

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class DolarResponse (
    @SerializedName("message") var dolares:JsonArray
)