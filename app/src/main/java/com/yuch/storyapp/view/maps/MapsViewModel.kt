package com.yuch.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.data.UserRepository
import com.yuch.storyapp.data.response.ListStoryItem

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getMapsStories(): LiveData<ResultState<List<ListStoryItem>>> = repository.getStoriesWithLocation()
}