package com.example.testtask.util

import android.app.Application
import com.example.testtask.api.BookServiceRepository
import com.example.testtask.database.UserRepository


class BaseApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Profile.initialize()
        DataStoreManager.initialize(this)
        UserRepository.initialize(this)
        BookServiceRepository.initialize()
    }
}