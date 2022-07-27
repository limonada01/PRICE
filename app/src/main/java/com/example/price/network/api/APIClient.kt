package com.example.price.network.api

import com.example.price.Dolares
import com.example.price.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIClient {
    @GET("valorTiposDeDolarHoy")
    suspend fun getDolaresInicio(): Response<Dolares>
    @GET("valoresHistoricosDolar/paginacion/{tipoDolar}/1/10")
    suspend fun getDolarHistorico(@Path("tipoDolar") tipoDolar: String): Response<Dolares>
    @POST("valoresHistoricosDolar")
    suspend fun postDolarHistorico(@Body requestBody: RequestBody): Response<Dolares>
}