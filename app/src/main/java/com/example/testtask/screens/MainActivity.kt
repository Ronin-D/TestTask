package com.example.testtask.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testtask.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment==null){
            val fragmnet = RecommendationsFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragmnet).commit()
        }

    }

}