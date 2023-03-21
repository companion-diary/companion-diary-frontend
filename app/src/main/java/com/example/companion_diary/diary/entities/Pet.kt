package com.example.companion_diary.diary.entities

import com.google.gson.annotations.SerializedName

data class PetList(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: ArrayList<Pet>
)

data class Pet(
    @SerializedName("pet_id")
    val petId: Int,
    @SerializedName("pet_tag")
    val petTag: String,
    @SerializedName("pet_name")
    val petName: String,
    @SerializedName("pet_age")
    val petAge: Int,
    @SerializedName("pet_species")
    val petSpecies: String,
    @SerializedName("pet_sex")
    val petSex: String,
    @SerializedName("pet_profile_img")
    val petProfileImg: String
): java.io.Serializable