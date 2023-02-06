package com.example.companion_diary.diary.network

import com.example.companion_diary.diary.entities.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryApi {
    @GET("/diarys/list/date")
    fun getDateList(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<Date>

    @GET("/diarys")
    fun getDiaryList(
        @Query("date") date: String
    ): Call<DiaryPreviewList>

    @GET("/users/pet")
    fun getPetList(): Call<PetList>

    @POST("/diarys")
    fun createDiary(
        @Body diary: Diary
    ): Call<DiaryResponse>
}