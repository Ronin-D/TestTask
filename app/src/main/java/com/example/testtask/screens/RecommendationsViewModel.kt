package com.example.testtask.screens

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtask.database.UserRepository
import com.example.testtask.models.Book
import com.example.testtask.models.SwipeModel
import com.example.testtask.models.User
import com.example.testtask.util.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class RecommendationsViewModel : ViewModel() {

    private val userRepository = UserRepository.get()
    private val dataStoreManager = DataStoreManager.get()


    fun initUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveUserId(user.uid)
            userRepository.addUser(user)
        }
    }

    fun getUser():LiveData<User?>{
        val result = MutableLiveData<User?>()
        var user:User? = null
        viewModelScope.launch(Dispatchers.IO) {
            val uid = dataStoreManager.getSavedUserId()
            user = userRepository.getUser(uid)
        }.invokeOnCompletion { result.postValue(user) }
        return result
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun isDbEmpty() = userRepository.isDbEmpty()

    val books = MutableLiveData(
        listOf(
            Book(photo = Color.BLUE, title = "Robinson Crusoe", author = "Daniel Defo", countOfPages = 150,genre = "Adventure"),
            Book(photo = Color.RED, title = "Sport programming", author = "Den Star", countOfPages = 150,genre = "Romantic"),
            Book(photo = Color.CYAN, title = "How to pass exams", author = "Pavel Vladimirovich Zinoviev", countOfPages = 150,genre = "Detective"),
            Book(photo = Color.GREEN, title = "Hello world", author = "some dude", countOfPages = 150,genre = "Detective"),
            Book(photo = Color.LTGRAY, title = "Naruto", author = "Masashi Kishimoto", countOfPages = 150,genre = "Adventure"),
            Book(photo = Color.MAGENTA, title = "Surviving in D corpus", author = "some student", countOfPages = 150,genre = "Adventure")
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