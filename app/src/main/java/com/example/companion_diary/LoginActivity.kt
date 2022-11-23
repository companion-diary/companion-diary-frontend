package com.example.companion_diary

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.companion_diary.api.TokenNetworkService
import com.example.companion_diary.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var kakaoAuthViewModel: KakaoAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                kakaoAuthViewModel = ViewModelProvider(this).get(KakaoAuthViewModel::class.java)
                kakaoAuthViewModel.currentToken.observe(this, Observer {
                    if (it.isNotEmpty()) {
                        //토큰 전송 함수 호출
                        //토큰 전송 후 토큰 유효 여부를 확인한 후에 페이지 이동

                        if (it != "error") {
                            lifecycleScope.launch {

                                val result = withContext(Dispatchers.IO) {

                                    TokenNetworkService.auth_token = it
                                    TokenNetworkService.service.getTokenResult()
                                }
                                val answer = result.result.jwt
                                if (answer.isNotEmpty()) {
                                    Log.d("answer", answer)
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.d("answer", "0")
                                }
                            }
                        }

                    }
                })
            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.loginBtn.setOnClickListener {
            kakaoAuthViewModel.kakaoLogin()
        }


    }

}



