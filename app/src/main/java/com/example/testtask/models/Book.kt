package com.example.testtask.models

import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.gson.annotations.SerializedName

data class Book(
    var title:String = "",
    var author:String = "",
    @SerializedName("pagesСount")var countOfPages:Int = 0,
    var genre:String = "",
    var year:Int = 0,
    var description:String = "",
    var imageUrl:String = ""
)