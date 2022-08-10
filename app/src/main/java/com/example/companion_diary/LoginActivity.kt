package com.example.companion_diary

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.companion_diary.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var kakaoAuthViewModel : KakaoAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kakaoAuthViewModel = ViewModelProvider(this).get(KakaoAuthViewModel::class.java)

        kakaoAuthViewModel.currentToken.observe(this, Observer{
            Log.d(TAG, "토큰 받아옴 : ${it}")
        })


        binding.loginBtn.setOnClickListener{
            kakaoAuthViewModel.kakaoLogin()
        }

//        KakaoLoginView(kakaoAuthViewModel, binding.loginBtn)

    }



}

//private fun KakaoLoginView(viewModel: KakaoAuthViewModel, LoginBtn : ImageButton){
//
//
//    val tokenInfo = viewModel.tokenInfo
//    Log.d(TAG,"토큰 발송 ${tokenInfo.value}")
//
//    LoginBtn.setOnClickListener{
//        viewModel.kakaoLogin()
//    }
//
//}