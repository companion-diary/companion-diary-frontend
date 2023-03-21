package com.example.companion_diary.login

import android.app.Application
import com.example.companion_diary.BuildConfig
import com.example.companion_diary.utils.Preferences
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {

    companion object{
        lateinit var prefsManager : Preferences
    }

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들
        prefsManager = Preferences(applicationContext)
        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}