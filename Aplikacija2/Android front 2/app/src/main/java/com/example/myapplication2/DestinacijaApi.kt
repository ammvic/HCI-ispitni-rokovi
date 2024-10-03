package com.example.myapplication2

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DestinacijaApi {
    @GET("api/Destinacije")
    suspend fun getDestinacije(): List<Destinacija>

    @GET("api/Destinacije/{id}")
    suspend fun getDestinacija(@Path("id") id: Int): Destinacija

    @GET("api/Destinacije/search")
    suspend fun searchDestinacije(@Query("query") query: String): List<Destinacija>
}
