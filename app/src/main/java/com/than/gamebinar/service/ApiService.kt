package com.than.gamebinar.service

import com.than.gamebinar.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/v1/auth/login")
    fun loginUser(@Body request : LoginRequest): Call<LoginResponse>

    @POST("api/v1/auth/register")
    fun registerUser(@Body request : RegisterRequest): Call<RegisterResponse>

    @GET("api/v1/auth/me")
    fun getUser(@Header("Authorization") token: String): Call<RegisterResponse>

    @Multipart
    @PUT("api/v1/users")
    fun updateUser(@Header("Authorization") token: String,
                   @Part("username") username: RequestBody,
                   @Part("email") email: RequestBody
    ): Call<RegisterResponse>
}