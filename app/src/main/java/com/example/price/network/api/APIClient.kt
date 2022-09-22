package com.example.price.network.api

import com.example.price.Dolares
import com.example.price.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIClient {
    @GET("valorTiposDeDolarHoy")
    suspend fun getDollarHome(): Response<Dolares>
    @GET("valoresHistoricosDolar/paginacion/{tipoDolar}/1/{rango}")
    suspend fun getDollarHistory(@Path("tipoDolar") tipoDolar: String, @Path("rango") rango: Int): Response<Dolares>
    @POST("valoresHistoricosDolar")
    suspend fun postDollarHistory(@Body requestBody: RequestBody): Response<Dolares>
}