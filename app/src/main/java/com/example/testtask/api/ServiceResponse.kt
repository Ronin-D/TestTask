package com.example.testtask.api

import com.google.gson.annotations.SerializedName

class ServiceResponse {
    @SerializedName("data")
    lateinit var books:BookResponse
}