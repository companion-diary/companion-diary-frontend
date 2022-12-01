package com.example.companion_diary.login.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenNetworkService {

    private const val BASE_URL = "http://3.35.48.125:80/"

    var auth_token : String = ""

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-ACCESS-TOKEN", auth_token)
                .build()
            it.proceed(request)
        }
        .build()

    private val retrofit by lazy{
        Retrofit.Builder()
        .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val service : TokenApi by lazy {
        retrofit.create(TokenApi::class.java)
    }

    val jwtService : JwtApi by lazy {
        retrofit.create(JwtApi::class.java)
    }

}