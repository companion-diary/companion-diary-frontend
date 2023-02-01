package com.example.companion_diary.diary.entities

import com.google.gson.annotations.SerializedName

data class DiaryPreviewList(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: ArrayList<DiaryPreview>
)

data class DiaryPreview(
    @SerializedName("diary_id")
    val diaryId: Int,
    @SerializedName("pet_id")
    val petId: Int,
    @SerializedName("pet_name")
    val petName: String,
    @SerializedName("pet_profile_img")
    val petProfileImg: String,
    @SerializedName("pet_tag")
    val petTag: String,
    @SerializedName("diary_title")
    val diaryTitle: String,
    @SerializedName("diary_content")
    val diaryContent: String
)
