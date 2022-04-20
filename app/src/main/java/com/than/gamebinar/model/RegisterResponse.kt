package com.than.gamebinar.model


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("data")
    val data: RegisterData,
    @SerializedName("success")
    val success: Boolean
)
data class RegisterData(
    @SerializedName("email")
    val email: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("username")
    val username: String
)