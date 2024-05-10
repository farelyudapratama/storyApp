package com.yuch.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuch.storyapp.data.ResultState
import com.yuch.storyapp.data.UserRepository
import com.yuch.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registrationResult = MutableLiveData<ResultState<RegisterResponse>>()
    val registrationResult: LiveData<ResultState<RegisterResponse>> get() = _registrationResult

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            try {
                _registrationResult.value = ResultState.Loading
                val result = repository.register(name, email, password)
                _registrationResult.postValue(result)
            } catch (e: Exception){
                _registrationResult.postValue(ResultState.Error("${e.message}"))
            }
        }
    }
}