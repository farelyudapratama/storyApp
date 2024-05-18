package com.yuch.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import com.yuch.storyapp.data.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadStory(file: File, description: String, lat: Double?, lon: Double?) = repository.addStories(file, description, lat, lon)
}