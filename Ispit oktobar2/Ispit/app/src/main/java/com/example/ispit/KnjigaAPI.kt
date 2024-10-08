package com.example.ispit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KnjigaApi {
    @GET("api/Knjige")
    suspend fun getKnjige(): List<Knjiga>

    @GET("api/Knjige/{id}")
    suspend fun getKnjiga(@Path("id") id: Int): Knjiga

    @GET("api/Knjige/search")
    suspend fun searchKnjige(
        @Query("pretraga") pretraga: String?
    ): List<Knjiga>

}