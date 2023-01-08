package com.example.testtask.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtask.api.BookServiceRepository
import com.example.testtask.database.UserRepository
import com.example.testtask.models.SwipeModel
import com.example.testtask.models.User
import com.example.testtask.util.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendationsViewModel : ViewModel() {

    private val userRepository = UserRepository.get()
    private val dataStoreManager = DataStoreManager.get()
    private val bookServiceRepository = BookServiceRepository.get()

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

    val books = bookServiceRepository.fetchContents()

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