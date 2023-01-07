package com.example.testtask.api

import retrofit2.Call
import retrofit2.http.GET

interface BookServiceApi {
    @GET("/b/BUME")
    fun fetchBooks(): Call<ServiceResponse>
}