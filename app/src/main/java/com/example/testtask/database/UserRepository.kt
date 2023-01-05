package com.example.testtask.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testtask.database.UserDatabase
import com.example.testtask.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "user-database"
class UserRepository private constructor(context: Context) {

    private val database:UserDatabase = Room.databaseBuilder(
        context,UserDatabase::class.java, DATABASE_NAME
    ).build()

    private val userDao = database.userDao()

    fun getUser(uid:UUID) = userDao.getCurrentUser(uid)
    fun isDbEmpty() = userDao.isEmpty()
    fun updateUser(user:User,scope: CoroutineScope){
        scope.launch { userDao.updateUser(user) }
    }
    suspend fun addUser(user: User){
            userDao.addUser(user)
    }

    fun updateUser(user:User){
        userDao.updateUser(user)
    }

    companion object {
        private var INSTANCE: UserRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }
        }
        fun get(): UserRepository {
            return INSTANCE ?: throw IllegalStateException("UserRepository must be initialized")
        }
    }
}