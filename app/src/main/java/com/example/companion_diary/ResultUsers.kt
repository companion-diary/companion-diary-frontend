package com.example.companion_diary


import com.google.gson.annotations.SerializedName

data class ResultUsers(
    @SerializedName("user_email")
    var userEmail: String,
    @SerializedName("user_nickname")
    var userNickname: String,
    @SerializedName("user_profile_img")
    var userProfileImg: String
)