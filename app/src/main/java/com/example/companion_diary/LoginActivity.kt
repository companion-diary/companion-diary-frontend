package com.example.companion_diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.example.companion_diary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private val kakaoAuthViewModel : KakaoAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val Login = binding.loginBtn

        KakaoLoginView(kakaoAuthViewModel, Login)

    }
}

private fun KakaoLoginView(viewModel: KakaoAuthViewModel, LoginBtn : Button){

    LoginBtn.setOnClickListener{
        viewModel.handleKakaoLogin()
    }

}