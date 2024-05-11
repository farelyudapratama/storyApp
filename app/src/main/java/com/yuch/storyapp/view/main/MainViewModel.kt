package com.yuch.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.data.UserRepository
import com.yuch.storyapp.data.pref.UserModel
import com.yuch.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val listStoryItem = MutableLiveData<ResultState<StoryResponse>>()
    val storyItem: LiveData<ResultState<StoryResponse>> = listStoryItem

    init {
        getStories()
    }

    private fun getStories() {
        viewModelScope.launch {
            val storyResponse = repository.getStories()
            storyResponse.asFlow().collect {
                listStoryItem.value = it
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}