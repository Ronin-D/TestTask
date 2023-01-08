package com.example.testtask.api

import retrofit2.Call
import retrofit2.http.GET

interface BookServiceApi {
    @GET("/b/HJ53")
    fun fetchBooks(): Call<ServiceResponse>
}