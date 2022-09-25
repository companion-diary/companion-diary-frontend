package com.example.companion_diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companion_diary.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        //animal bottom sheet dialog 띄우기
        val bottomSheetView_animal = layoutInflater.inflate(R.layout.dialog_profile_animal_create, null)
        val bottomSheetDialog_animal = BottomSheetDialog(requireActivity())
        bottomSheetDialog_animal.setContentView(bottomSheetView_animal)

        bottomSheetDialog_animal.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED//반만 띄우기

        binding.fragmentProfileAnimalCreateBt.setOnClickListener{
            val bottomSheet_animal = BottomSheetDialogFragment_animal_create()
            bottomSheetDialog_animal.show()
        }
        //plant bottom sheet dialog 띄우기
        val bottomSheetView_plant = layoutInflater.inflate(R.layout.dialog_profile_plant_create, null)
        val bottomSheetDialog_plant = BottomSheetDialog(requireActivity())
        bottomSheetDialog_plant.setContentView(bottomSheetView_plant)

        bottomSheetDialog_plant.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED//반만 띄우기

        binding.fragmentProfilePlantCreateBt.setOnClickListener{
            val bottomSheet_plant = BottomSheetDialogFragment_plant_create()
            bottomSheetDialog_plant.show()
        }

        //제일 중요한 함수 실행
        getAnimalPlantData(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJFbWFpbCI6ImV1bjIwNTE2QG5hdmVyLmNvbSIsInVzZXJOaWNuYW1lIjoi67Cx7J2A6rK9IiwidXNlclByb2ZpbGVJbWFnZSI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9iaGdmZHIvYnRyUFFNeHVsVTEvbTg3NGRLVFFVbXBBYlF6ZWhtY0N2Sy9pbWdfNjQweDY0MC5qcGciLCJpYXQiOjE2Njc5OTY0NzQsImV4cCI6MTY5OTUzMjQ3NCwic3ViIjoidXNlckluZm8ifQ.leo8A2TGijtHdmDMkCl2__gkX7cD43lKOpREGUcYDDY"
        )
        return binding.root
    }

    private fun getAnimalPlantData(token : String){
        val animalPlantInterface = RetrofitClient.sRetrofit.create(AnimalPlantInterface::class.java)
        animalPlantInterface.getAnimalPlant(token).enqueue(object : Callback<AnimalPlantListResponse> {
            override fun onResponse(
                call: Call<AnimalPlantListResponse>,
                response: Response<AnimalPlantListResponse>
            ) {
                if(response.isSuccessful){
                    val result = response.body() as AnimalPlantListResponse
                    binding.fragmentProfileUsernameTv.text=result.message
                    Log.d("MainActivity",result.result.toString())
                    Log.d("MainActivity",result.toString())

                    //동물 정보json에서 가져와서 리사이클러뷰에 넣기
                    val allResult=result.result
                    val AnimalDatas = ArrayList<Animal>()//동물 리스트
                    var PlantDatas = ArrayList<Plant>()//식물 리스트트
                   for (i in 0 until allResult.size){
                       if(result.result[i].petTag=="ANIMAL"){
                           //동물데이터
                           AnimalDatas.apply {
                               add(Animal(result.result[i].petName,result.result[i].petSpecies,result.result[i].petAge.toString(),R.drawable.gender_animal,result.result[i].petProfileImg))
                           }
                       } else if(result.result[i].petTag=="PLANT"){
                           //식물데이터
                           PlantDatas.apply{
                               add(Plant(result.result[i].petName,result.result[i].petSpecies,result.result[i].petAge.toString()+"년생",result.result[i].petProfileImg))
                           }
                       }
                    }
                    //동물리사이클러뷰 연결
                    val animalRVAdapter= AnimalRVAdapter(AnimalDatas)
                    binding.fragmentProfileAnimalRv.adapter=animalRVAdapter
                    binding.fragmentProfileAnimalRv.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL,false)
                    //식물리사이클러뷰 연결
                    val plantRVAdapter= PlantRVAdapter(PlantDatas)
                    binding.fragmentProfilePlantRv.adapter=plantRVAdapter
                    binding.fragmentProfilePlantRv.layoutManager=
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                } else{
                    Log.d("MainActivity","getWeatherData - onResponse : Error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AnimalPlantListResponse>, t: Throwable) {
                Log.d("MainActivity",t.message ?:"통신오류")
            }

        })
    }
}