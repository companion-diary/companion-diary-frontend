package com.example.companion_diary.login.api

import retrofit2.http.*

interface TokenApi {
    @GET("/auth/kakao/callback")
    suspend fun getTokenResult(
    ): TokenResponse
}