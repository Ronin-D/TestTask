package com.example.testtask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User
    (
    @PrimaryKey val uid:UUID = UUID.randomUUID(),
    var likedBooksCnt:Int = 0,
    var readBooksCnt:Int = 0,
    var genres:MutableMap<String,Int> = mutableMapOf()
    )