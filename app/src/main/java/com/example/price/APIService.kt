package com.example.price

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getDolaresInicio(@Url url:String): Response<DolarResponse>
    @GET
    suspend fun getDolarHistorico(@Url url:String): Response<DolarHistoricoResponse>
    @POST
    suspend fun postDolaresInicio(@Url url:String): Response<DolarResponse>
}