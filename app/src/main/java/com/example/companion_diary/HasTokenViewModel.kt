package com.example.companion_diary

import android.annotation.SuppressLint
import androidx.lifecycle.AndroidViewModel
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.suspendCoroutine

class HasTokenViewModel(application: MyApplication) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HasTokenViewModel"
    }

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    private suspend fun handleHasToken() : String =

        suspendCoroutine<String> { continuation ->

            // 단말에 토큰이 있는지 검사
            if (AuthApiClient.instance.hasToken()) {

                // 서버에 유효한 AccessToken이 있는지 가져옴
                UserApiClient.instance.accessTokenInfo { _, error ->

                    // 현재 유효한 AccessToken이 없음
                    // AccessToken이 만료 된것 이라면 SDK 내부에서 AccessToken을 갱신 합니다.
                    if (error != null) {

                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                            // AccessToken 갱신까지 실패 한 것이기 때문에 RefreshToken이 유효하지 않음, 로그인 시도
                            
                        }
                        else {
                            //기타 에러

                        }
                    }
                    else {
                        //AccessToken 유효성 체크 성공(필요 시 SDK 내부에서 AccessToken 갱신됨)

                    }
                }
            }
            else {
                // 단말에 토큰이 없으니 로그인 시도

            }

        }

}