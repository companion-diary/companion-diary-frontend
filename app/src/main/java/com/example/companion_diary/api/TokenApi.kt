package com.example.companion_diary.api

import retrofit2.Call
import retrofit2.http.*

interface TokenApi {
    @GET("/auth/kakao/callback")
    suspend fun getTokenResult(
    ): TokenResponse
}