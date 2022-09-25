package com.example.companion_diary

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient2 {

    val sRetrofit = initRetrofit()
    private const val ANIMALPLANT_URL = "http://13.125.223.255:80/"

    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(ANIMALPLANT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}