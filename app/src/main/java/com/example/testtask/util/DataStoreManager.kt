package com.example.testtask.util

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.first
import java.util.UUID

private const val USER_ID_KEY = "user_id"
class DataStoreManager private constructor(context: Context){

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "account_data")

    suspend fun saveUserId(id:UUID){
        val dataStoreIdKey = preferencesKey<String>(USER_ID_KEY)
        dataStore.edit {
            it[dataStoreIdKey] = id.toString()
        }
    }

    suspend fun getSavedUserId():UUID{
        val dataStoreIdKey = preferencesKey<String>(USER_ID_KEY)
        val preferences = dataStore.data.first()
        return UUID.fromString(preferences[dataStoreIdKey])
    }

    companion object{
        private var INSTANCE: DataStoreManager? = null
        fun initialize(context: Context){
            if (INSTANCE ==null){
                INSTANCE = DataStoreManager(context)
            }
        }
        fun get(): DataStoreManager {
            return INSTANCE ?:throw IllegalStateException("Auth repository must be initialized")
        }
    }
}