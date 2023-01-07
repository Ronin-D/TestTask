package com.example.testtask.api

import com.example.testtask.models.Book
import com.google.gson.annotations.SerializedName

class BookResponse {
    @SerializedName("books")
    lateinit var books:List<Book>

}