package com.example.companion_diary.api

import android.media.session.MediaSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenNetworkService {

    private const val BASE_URL = "http://220.84.249.183:13000/"

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