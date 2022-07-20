package com.example.price

import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getDolaresInicio(@Url url:String): Response<DolarResponse>
    @GET
    suspend fun getDolarHistorico(@Url url:String): Response<DolarHistoricoResponse>
    @POST("valoresHistoricosDolar")
    suspend fun postDolarHistorico(@Body requestBody: RequestBody): Response<DolarResponse>
}