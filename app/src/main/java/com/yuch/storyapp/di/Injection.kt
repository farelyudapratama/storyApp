package com.yuch.storyapp.di

import android.content.Context
import com.yuch.storyapp.data.UserRepository
import com.yuch.storyapp.data.api.ApiConfig
import com.yuch.storyapp.data.database.StoryDatabase
import com.yuch.storyapp.data.pref.UserPreference
import com.yuch.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val dataBase = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(dataBase,apiService, pref)
    }
}