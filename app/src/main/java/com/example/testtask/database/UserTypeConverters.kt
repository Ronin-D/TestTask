package com.example.testtask.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.util.*


class UserTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toMap(value:String?):MutableMap<String,Int>{
       return Gson().fromJson(value, object : TypeToken<MutableMap<String, Int>>() {}.type)
    }

    @TypeConverter
    fun fromMap(map:MutableMap<String,Int>?):String{
        return  Gson().toJson(map)
    }

}