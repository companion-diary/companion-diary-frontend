package com.example.companion_diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.companion_diary.databinding.FragmentSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        Log.d("settingFragment","어디까지되는지 테스트")
        //제일 중요한 함수 실행
        getUsersData(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJFbWFpbCI6ImV1bjIwNTE2QG5hdmVyLmNvbSIsInVzZXJOaWNuYW1lIjoi67Cx7J2A6rK9IiwidXNlclByb2ZpbGVJbWFnZSI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9iaGdmZHIvYnRyUFFNeHVsVTEvbTg3NGRLVFFVbXBBYlF6ZWhtY0N2Sy9pbWdfNjQweDY0MC5qcGciLCJpYXQiOjE2Njc5OTY0NzQsImV4cCI6MTY5OTUzMjQ3NCwic3ViIjoidXNlckluZm8ifQ.leo8A2TGijtHdmDMkCl2__gkX7cD43lKOpREGUcYDDY"
        )

        return binding.root
    }

    private fun getUsersData(token : String){
        val usersInterface = RetrofitClient2.sRetrofit.create(UsersInterface::class.java)
        usersInterface.getUsers(token).enqueue(object :
            Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                if(response.isSuccessful){
                    val result = response.body() as UsersResponse
                    binding.fragmentSettingUserProfileIdTv.text=result.result.userEmail
                    binding.fragmentSettingUserProfileNicknameTv.text=result.result.userNickname
                    Log.d("settingFragment","어디까지되는지 테스트2")
                    Glide.with(this@SettingFragment).load(result.result.userProfileImg).into(binding.fragmentSettingUserProfileImg)
                } else{
                    Log.d("MainActivity","getWeatherData - onResponse : Error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.d("MainActivity",t.message ?:"통신오류")
            }

        })
    }
}