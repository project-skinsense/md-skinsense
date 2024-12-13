package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.AppDatabase
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = AppDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository(database.userDao(), apiService)
    }
}