package com.yuch.storyapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.data.UserRepository
import com.yuch.storyapp.data.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val storyDetail = MutableLiveData<ResultState<DetailStoryResponse>>()
    val story: LiveData<ResultState<DetailStoryResponse>> = storyDetail

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            val storyResponse = repository.getDetailStories(id)
            storyResponse.asFlow().collect {
                storyDetail.value = it
            }
        }
    }
}