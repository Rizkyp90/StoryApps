package com.example.data.network

import com.example.storyapps.api.LoginRequest
import com.example.storyapps.api.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
} 