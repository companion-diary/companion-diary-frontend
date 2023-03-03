package com.example.companion_diary.diary.network

import android.util.Log
import com.example.companion_diary.diary.CalendarFragment
import com.example.companion_diary.diary.entities.Date
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object DiaryClient {
    private var instance: Retrofit? = null
    private val gson = GsonConverterFactory.create()

    val xAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJFbWFpbCI6ImV1bjIwNTE2QG5hdmVyLmNvbSIsInVzZXJOaWNuYW1lIjoi67Cx7J2A6rK9IiwidXNlclByb2ZpbGVJbWFnZSI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9iaGdmZHIvYnRyUFFNeHVsVTEvbTg3NGRLVFFVbXBBYlF6ZWhtY0N2Sy9pbWdfNjQweDY0MC5qcGciLCJpYXQiOjE2Njc5OTY0NzQsImV4cCI6MTY5OTUzMjQ3NCwic3ViIjoidXNlckluZm8ifQ.leo8A2TGijtHdmDMkCl2__gkX7cD43lKOpREGUcYDDY"
    val TAG = "DiaryClient"

    private const val BASE_URL = "http://13.125.223.255:80/"

    val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-ACCESS-TOKEN", xAccessToken)
                .build()
            it.proceed(request)
        }
        .build()

    private fun getInstance(): Retrofit {
        if(instance == null){
            instance = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build()
        }
        return instance!!
    }
    
    val diaryService: DiaryApi = getInstance().create(DiaryApi::class.java)
}