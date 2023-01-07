package com.example.testtask.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtask.models.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TEST_URL = "https://www.jsonkeeper.com/"
class BookServiceRepository {

    private val bookServiceApi:BookServiceApi

    init {
        val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl(TEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
         bookServiceApi = retrofit.create(BookServiceApi::class.java)
    }

    fun fetchContents():LiveData<List<Book>?>{
        val responseLiveData = MutableLiveData<List<Book>?>()
        val request = bookServiceApi.fetchBooks()
        request.enqueue(object:Callback<ServiceResponse>{
            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                //do something else
                responseLiveData.postValue(null)
            }

            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                val serviceResponse = response.body()
                val bookResponse = serviceResponse?.books
                val books = bookResponse?.books?: mutableListOf()
                responseLiveData.postValue(books)
            }
        })
        return responseLiveData
    }

    companion object{
        private var INSTANCE: BookServiceRepository? = null
        fun initialize(){
            if (INSTANCE ==null){
                INSTANCE =  BookServiceRepository()
            }
        }
        fun get():  BookServiceRepository {
            return INSTANCE ?:throw IllegalStateException("BookServiceRepository must be initialized")
        }
    }

}