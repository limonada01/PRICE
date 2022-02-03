package com.example.price

import com.google.gson.annotations.SerializedName

data class DolarResponse (
    @SerializedName("dolares") var message:List<String>
)