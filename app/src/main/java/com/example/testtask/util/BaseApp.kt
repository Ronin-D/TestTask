package com.example.testtask.util

import android.app.Application
import com.example.testtask.database.UserRepository


class BaseApp:Application() {
    override fun onCreate() {
        super.onCreate()
        UserRepository.initialize(this)
    }
}