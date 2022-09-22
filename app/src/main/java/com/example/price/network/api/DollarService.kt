package com.example.price.network.api

import com.example.price.Dolar
import com.example.price.RequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DollarService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getDollarHome(): MutableList<Dolar> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(APIClient::class.java).getDollarHome()
            (response.body())?.dolares ?: mutableListOf<Dolar>()
        }
    }

    suspend fun getDollarHistory(tipoDolar: String, rango: Int): MutableList<Dolar> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(APIClient::class.java).getDollarHistory(tipoDolar, rango)
            (response.body())?.dolares ?: mutableListOf()
        }
    }
    suspend fun postDollarHistory(dolar: RequestBody){
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(APIClient::class.java).postDollarHistory(dolar)
            (response.body())?.dolares ?: mutableListOf<Dolar>()
        }
    }
}