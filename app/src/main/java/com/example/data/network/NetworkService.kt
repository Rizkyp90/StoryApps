package com.example.data.network

import javax.inject.Inject
import com.example.storyapps.api.LoginRequest
import com.example.storyapps.api.LoginResponse
import com.example.storyapps.utils.EspressoIdlingResource

class NetworkService @Inject constructor(private val apiService: ApiService) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        EspressoIdlingResource.increment()
        return try {
            val response = apiService.login(LoginRequest(email, password))
            EspressoIdlingResource.decrement()
            Result.success(response)
        } catch (e: Exception) {
            EspressoIdlingResource.decrement()
            Result.failure(e)
        }
    }

    suspend fun logout() {
        EspressoIdlingResource.increment()
        try {
            EspressoIdlingResource.decrement()
        } catch (e: Exception) {
            EspressoIdlingResource.decrement()
            throw e
        }
    }
} 