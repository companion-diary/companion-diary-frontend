package com.example.companion_diary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AnimalPlantInterface {
    @GET("/users/pet")
    fun getAnimalPlant(
        @Header("x-access-token") token: String?
    ) : Call<AnimalPlantListResponse>
}