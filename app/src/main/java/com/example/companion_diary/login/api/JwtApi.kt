package com.example.companion_diary.login.api

import retrofit2.http.GET

interface JwtApi {
    @GET("/auth/jwt")
    suspend fun getJwtTokenResult(
    ): TokenResponse
}