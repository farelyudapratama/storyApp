package com.yuch.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import com.yuch.storyapp.data.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.addStories(file, description)
}