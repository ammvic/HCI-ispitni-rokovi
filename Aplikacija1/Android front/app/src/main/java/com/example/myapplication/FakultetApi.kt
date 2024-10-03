package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FakultetApi {

    @POST("api/Profesor/register")
    fun register(@Body profesor: Profesor): Call<Profesor>

    @POST("api/Profesor/login")
    fun login(@Body profesor: Profesor): Call<Profesor>

    @GET("api/Profesor/{id}/predmeti")
    fun getPredmeti(@Path("id") profesorId: Int): Call<List<Predmet>>
}
