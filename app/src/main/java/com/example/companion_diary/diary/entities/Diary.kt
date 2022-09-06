package com.example.companion_diary.diary.entities

import android.net.Uri

data class Diary(
    var date: String,
    var title: String,
    var content: String,
    var tag: String, // 또는 동식물 data class
    var imgList: ArrayList<Uri>
)
