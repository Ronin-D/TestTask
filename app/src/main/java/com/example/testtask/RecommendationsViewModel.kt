package com.example.testtask

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.MutableLiveData
import com.example.testtask.models.Book
import com.example.testtask.models.SwipeModel

class RecommendationsViewModel : ViewModel() {
    val books = MutableLiveData(
        listOf(
            Book(photo = Color.BLUE),
            Book(photo = Color.RED),
            Book(photo = Color.CYAN),
            Book(photo = Color.GREEN),
            Book(photo = Color.LTGRAY),
            Book(photo = Color.MAGENTA)
        )
    )

    private var currentIndex:Int = 0

    private val topCard
        get() = books.value?.get(currentIndex)

    private val bottomCard
        get() = books.value?.get(currentIndex+1)

    val swipeModel = MutableLiveData<SwipeModel>()

    fun swipe(){
        currentIndex++
        updateCards()
    }

    fun updateCards(){
        if (currentIndex+1<books.value!!.size){
            swipeModel.value = SwipeModel(
                topCard, bottomCard
            )
        }
        else if (currentIndex<books.value!!.size){
            swipeModel.value = SwipeModel(
                topCard, null
            )
        }
        else{
            swipeModel.value = SwipeModel(
                null, null
            )
        }
    }
}