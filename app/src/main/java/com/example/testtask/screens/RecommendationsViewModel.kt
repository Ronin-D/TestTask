package com.example.testtask.screens

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtask.database.UserRepository
import com.example.testtask.models.Book
import com.example.testtask.models.SwipeModel
import com.example.testtask.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendationsViewModel : ViewModel() {

    private val userRepository = UserRepository.get()

    fun initDatabaseData(){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(User())
        }
    }

    fun isDbEmpty() = userRepository.isDbEmpty()

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