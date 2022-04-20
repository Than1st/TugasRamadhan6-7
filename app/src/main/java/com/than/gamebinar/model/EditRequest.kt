package com.than.gamebinar.model


import com.google.gson.annotations.SerializedName

data class EditRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String

)