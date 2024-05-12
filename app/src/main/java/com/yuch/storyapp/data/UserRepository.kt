package com.yuch.storyapp.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.yuch.storyapp.data.api.ApiConfig
import com.yuch.storyapp.data.api.ApiService
import com.yuch.storyapp.data.pref.UserModel
import com.yuch.storyapp.data.pref.UserPreference
import com.yuch.storyapp.data.response.ErrorResponse
import com.yuch.storyapp.data.response.LoginResponse
import com.yuch.storyapp.data.response.RegisterResponse
import com.yuch.storyapp.data.response.UploadStoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    suspend fun logout(){
        userPreference.logout()
    }
    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse>{
        ResultState.Loading
        return try{
            val response = apiService.register(name, email, password)
            if (response.error == true) {
                ResultState.Error(response.message ?: "Unknown Error")
            } else {
                ResultState.Success(response)
            }
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage.toString())
        }
    }

    suspend fun login(email: String, password: String): ResultState<LoginResponse>{
    ResultState.Loading
        try {
            val response = apiService.login(email, password)
            if (response.error == true) {
                return ResultState.Error(response.message ?: "Unknown Error")
            } else {
                val loginResult = response.loginResult
                if (loginResult != null) {
                    val sesi = UserModel(
                        email = email,
                        token = loginResult.token ?: "",
                        isLogin = true
                    )
                    saveSession(sesi)
//                    ApiConfig.token = loginResult.token ?: ""
                    ApiConfig.getApiService(sesi.token)
                    return ResultState.Success(response)
                } else {
                    return ResultState.Error("Login is nulll")
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            return ResultState.Error(errorMessage ?: "Unknown Error")
        }
    }

    fun getStories() = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStories()
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage.toString())
        }
    }

    fun getDetailStories(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getDetailStory(id)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(ResultState.Error(errorMessage.toString()))
        }
    }

    fun addStories(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.addStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadStoryResponse::class.java)
            emit(ResultState.Error("Error: $errorResponse"))
        }
    }

    companion object{
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
