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
    val diaryContent: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("diary_img_url_1")
    val diaryImgUrl1: String? = null,
    @SerializedName("diary_img_url_2")
    val diaryImgUrl2: String? = null,
    @SerializedName("diary_img_url_3")
    val diaryImgUrl3: String? = null,
    @SerializedName("diary_img_url_4")
    val diaryImgUrl4: String? = null,
    @SerializedName("diary_img_url_5")
    val diaryImgUrl5: String? = null,
): java.io.Serializable

data class Diary(
    @SerializedName("pet_id")
    val petId: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("diary_title")
    val diaryTitle: String,
    @SerializedName("diary_content")
    val diaryContent: String,
    @SerializedName("diary_img_url_1")
    val diaryImgUrl1: String? = null,
    @SerializedName("diary_img_url_2")
    val diaryImgUrl2: String? = null,
    @SerializedName("diary_img_url_3")
    val diaryImgUrl3: String? = null,
    @SerializedName("diary_img_url_4")
    val diaryImgUrl4: String? = null,
    @SerializedName("diary_img_url_5")
    val diaryImgUrl5: String? = null,
)

data class DiaryResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
