package com.example.testtask.models

import android.graphics.Color
import androidx.annotation.ColorInt

data class Book(
    var title:String = "",
    var author:String = "",
    var countOfPages:Int = 0,
    @ColorInt var photo:Int = Color.BLACK
)