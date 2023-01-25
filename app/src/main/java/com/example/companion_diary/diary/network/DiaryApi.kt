package com.example.companion_diary.diary.network

import com.example.companion_diary.diary.entities.Date
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DiaryApi {
    @GET("/diarys/list/date")
    fun getDateList(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<Date>
}