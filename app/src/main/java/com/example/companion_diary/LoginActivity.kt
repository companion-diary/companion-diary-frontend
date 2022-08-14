package com.example.companion_diary

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
            if(it.isNotEmpty()){
                //토큰 전송 함수 호출
                //토큰 전송 후 토큰 유효 여부를 확인한 후에 페이지 이동
            }else {

//                val builder = AlertDialog.Builder(this)
//
//                builder.setTitle("로그인 오류")
//                    .setMessage("로그인에 실패하였습니다. 로그인을 다시 진행해주세요.")
//                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialog , i ->
//                    })
//                val alertDialog = builder.create()
//                alertDialog.show()

            }
        })


        binding.loginBtn.setOnClickListener{
            kakaoAuthViewModel.kakaoLogin()

        }

//        KakaoLoginView(kakaoAuthViewModel, binding.loginBtn)

    }



}

