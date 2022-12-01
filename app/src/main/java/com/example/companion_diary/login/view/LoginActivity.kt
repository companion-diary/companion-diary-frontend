package com.example.companion_diary.login.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.companion_diary.MainActivity
import com.example.companion_diary.databinding.ActivityLoginBinding
import com.example.companion_diary.login.api.TokenNetworkService
import com.example.companion_diary.login.model.KakaoAuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var kakaoAuthViewModel: KakaoAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (tokenInfo != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

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
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
                else if(it == "error"){
                    loginAlertDialog(this)
                }

            }
        })

        binding.loginBtn.setOnClickListener {
            kakaoAuthViewModel.kakaoLogin()
        }


    }

    private fun loginAlertDialog(context : Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("로그인 오류")
            .setMessage("카카오 로그인 오류가 발생하였습니다.\n다시 로그인해주세요.")
            .setPositiveButton("확인") { dialog, which ->
            }
            .show()
    }

}



