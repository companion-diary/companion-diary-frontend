package com.example.companion_diary.diary.entities

import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: ArrayList<String>
)
