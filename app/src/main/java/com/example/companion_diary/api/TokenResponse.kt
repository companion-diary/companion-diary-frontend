package com.example.companion_diary.api

import com.google.gson.annotations.SerializedName

data class TokenResponse (

    @SerializedName("result") val result : JwtData

)

data class JwtData (

    @SerializedName("x-access-token") val jwt : String

    )