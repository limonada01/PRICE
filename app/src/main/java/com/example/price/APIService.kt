package com.example.price

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getDolaresInicio(@Url url:String): Response<DolarResponse>
    suspend fun getDolarHistorico(@Url url:String): Response<DolarHistoricoResponse>
}