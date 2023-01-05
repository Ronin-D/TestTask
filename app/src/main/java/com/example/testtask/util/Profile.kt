package com.example.testtask.util

import com.example.testtask.models.User

class Profile {
    var user:User? = null
    companion object{
        private var INSTANCE: Profile? = null
        fun initialize(){
            if (INSTANCE ==null){
                INSTANCE = Profile()
            }
        }
        fun get(): Profile {
            return INSTANCE ?:throw IllegalStateException("Profile must be initialized")
        }
    }
}