package com.example.companion_diary


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("pet_age")
    var petAge: Int,
    @SerializedName("pet_id")
    var petId: Int,
    @SerializedName("pet_name")
    var petName: String,
    @SerializedName("pet_profile_img")
    var petProfileImg: String,
    @SerializedName("pet_sex")
    var petSex: String,
    @SerializedName("pet_species")
    var petSpecies: String,
    @SerializedName("pet_tag")
    var petTag: String
)