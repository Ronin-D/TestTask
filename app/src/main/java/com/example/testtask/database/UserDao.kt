package com.example.testtask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.testtask.models.User
import java.util.UUID


@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE uid = (:id)")
    fun getCurrentUser(id:UUID):User?

    @Update
    fun updateUser(user: User)

    @Insert
    fun addUser(user: User)

    @Query("SELECT (SELECT COUNT(*) FROM User) == 0")
    fun isEmpty(): LiveData<Boolean>
}