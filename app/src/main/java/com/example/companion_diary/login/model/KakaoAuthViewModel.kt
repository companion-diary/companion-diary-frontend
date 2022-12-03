package com.example.companion_diary.login.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoAuthViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "KakaoAuthViewModel"
    }

    private val context = application.applicationContext

    private val tokenInfo = MutableLiveData<String>()
    private val isDataLoaded = MutableLiveData<Boolean>()

    val currentToken: LiveData<String>
        get() = tokenInfo

    //초기값
    init {
        tokenInfo.value = ""
    }

    fun kakaoLogin() {
        viewModelScope.launch {
            tokenInfo.value = handleKakaoLogin()
        }
    }

//    private suspend fun handleHasToken(): String =
//
//        suspendCoroutine<String> { continuation ->
//            // 단말에 토큰이 있는지 검사
//            if (AuthApiClient.instance.hasToken()) {
//                // 서버에 유효한 AccessToken이 있는지 가져옴
//                UserApiClient.instance.accessTokenInfo { _, error ->
//                    // 현재 유효한 AccessToken이 없음
//                    // AccessToken이 만료 된것 이라면 SDK 내부에서 AccessToken을 갱신 합니다.
//                    if (error != null) {
//                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
//                            // AccessToken 갱신까지 실패 한 것이기 때문에 RefreshToken이 유효하지 않음, 로그인 시도
//
//                        } else {
//                            //기타 에러
//
//                        }
//                    } else {
//                        //AccessToken 유효성 체크 성공(필요 시 SDK 내부에서 AccessToken 갱신됨)
//
//                    }
//                }
//            } else {
//                // 단말에 토큰이 없으니 로그인 시도
//            }
//
//        }


    private suspend fun handleKakaoLogin(): String =

        suspendCoroutine<String> { continuation ->

            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                    continuation.resume("error")
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    continuation.resume(token.accessToken)
                }
            }


            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
                        continuation.resume("error")

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        continuation.resume(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }


}