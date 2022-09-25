package com.example.companion_diary


import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("isSuccess")
    var isSuccess: Boolean,
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: ResultUsers
)