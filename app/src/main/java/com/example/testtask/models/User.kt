package com.example.testtask.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User
    (
    @PrimaryKey val uid:UUID = UUID.randomUUID(),
    var likedBooksCnt:Int = 0,
    val genres:MutableMap<String,Int> = mutableMapOf()
    )