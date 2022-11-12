package com.example.companion_diary.api

import retrofit2.http.GET

interface JwtApi {
    @GET("/auth/jwt")
    suspend fun getJwtTokenResult(
    ): TokenResponse
}