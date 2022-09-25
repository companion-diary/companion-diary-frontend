package com.example.companion_diary

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.inflate
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.DrawableCompat.inflate
import com.example.companion_diary.databinding.ActivityMainBinding
import com.example.companion_diary.databinding.ActivityMainBinding.inflate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.normal.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import java.security.KeyStore
import com.example.companion_diary.BottomSheetDialogFragment_animal_create
import com.example.companion_diary.databinding.ActivityMainBinding.bind
import com.example.companion_diary.databinding.DialogProfileAnimalCreateBinding

class BottomSheetDialogFragment_animal_create(Context context) : BottomSheetDialogFragment() {

    lateinit var binding: DialogProfileAnimalCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = DialogProfileAnimalCreateBinding.inflate(layoutInflater, container, false)


        //버튼 온오프

        /*

        val toggle: ToggleButton = v.findViewById(R.id.dialog_profile_animal_create_female_tbt)

        toggle.setOnCheckedChangeListener { ToggleButton, isChecked ->
            if (isChecked) {
                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_female_on)
                Log.d("g","버튼눌림")
            } else {
                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_female_off)
                Log.d("g","버튼눌림")
            }
        }*/


        /*
        //버튼이 온인지 오프인지 불리언으로 저장
        val female_bool=1
        val male_bool=1
        val toggle_female: ToggleButton = v.findViewById(R.id.dialog_profile_animal_create_female_tbt)
        toggle_female.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_female_on)
                Log.d("g","버튼눌림")
            } else {
                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_female_off)
                Log.d("g","버튼눌림")
            }
        }

        val toggle_male: ToggleButton = v.findViewById(R.id.dialog_profile_animal_create_male_tbt)
        toggle_male.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_male_on)
                Log.d("g","버튼눌림")
            } else {
                v.setBackgroundResource(R.drawable.dialog_profile_animal_create_male_off)
                Log.d("g","버튼눌림")
            }
        }

         */
        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        /*view?.findViewById<Button>(R.id.button_bottom_sheet)?.setOnClickListener {
            dismiss()
        }*/
    }

    // 카메라 접근 권한 확인용
    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            TedPermission.create()
                .setPermissionListener(this)
                .setRationaleMessage("카메라 접근 허용이 필요한 서비스입니다")
                .setDeniedMessage("[설정] → [권한]에서 권한 변경이 가능합니다")
                .setDeniedCloseButtonText("닫기")
                .setGotoSettingButtonText("설정")
                .setRationaleTitle("권한 설정")
                .setPermissions(
                    WRITE_EXTERNAL_STORAGE
                )
                .check()
        }
    }

    // 권한 허용된 상태면 이미지 피커 사용 가능하게
    override fun onPermissionGranted() {
        /**
         * image picker height 설정을 위해 계산
         */
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val activityHeight =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.height() - insets.bottom - insets.top
            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        val imagePickerHeight = activityHeight / 16 * 9 // 화면의 9/16 비율만큼만 자리를 차지하도록 설정
        /**
         * image picker
         */
        TedBottomPicker.with(this)
            .setPeekHeight(imagePickerHeight)
            .showTitle(false)
            .setCompleteButtonText("등록")
            .setSelectMaxCount(5)
            .setSpacing(10)
            .setEmptySelectionText("")
            .setSelectMaxCountErrorText("사진은 5장까지 등록 가능합니다")
            .showCameraTile(false)
            .setIncludeEdgeSpacing(true)
            .setSelectedUriList(imgList)
            .showMultiImage(object: TedBottomSheetDialogFragment.OnMultiImageSelectedListener{
                override fun onImagesSelected(uriList: MutableList<Uri>?) {
                    if(uriList == null) return
                    imgList.clear()
                    imgList.addAll(uriList)
                    imgListAdapter.notifyDataSetChanged()
                }
            })
    }

    // 카메라 접근 권한 없을때
    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this, "카메라 접근 권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
    }

}