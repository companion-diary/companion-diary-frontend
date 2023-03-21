package com.example.companion_diary.diary

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.ActionBar
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.MainActivity
import com.example.companion_diary.ProfileFragment
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ActivityMainBinding
import com.example.companion_diary.databinding.ActivityWriteDiaryBinding
import com.example.companion_diary.diary.custom.DayItemView.Companion.selectDateDay
import com.example.companion_diary.diary.custom.DayItemView.Companion.selectDateMonth
import com.example.companion_diary.diary.custom.DayItemView.Companion.selectDateYear
import com.example.companion_diary.diary.entities.Diary
import com.example.companion_diary.diary.entities.DiaryResponse
import com.example.companion_diary.diary.entities.Pet
import com.example.companion_diary.diary.entities.PetList
import com.example.companion_diary.diary.network.DiaryClient
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import org.joda.time.DateTime
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WriteDiaryActivity:AppCompatActivity(), PermissionListener {

    lateinit var binding : ActivityWriteDiaryBinding
    lateinit var imgList : MutableList<Uri>
    lateinit var imgListManager: LinearLayoutManager
    lateinit var imgListAdapter: DiaryImgRVAdapter
    lateinit var mIntent : Intent
    lateinit var pet: Pet
    val TAG = "WriteDiaryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteDiaryBinding.inflate(layoutInflater)
        imgList = ArrayList<Uri>()

        initView()
        initClickListener()
        initImgRecyclerView()

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        backButtonPressed()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun initView(){
        mIntent = intent
        pet = mIntent.getSerializableExtra("pet") as Pet

        /**
         * 동식물 구분해서 background resource 변경
         */
        binding.yearMonthDateTv.text = "${selectDateYear}년 ${selectDateMonth}월 ${selectDateDay}일"

        Glide.with(binding.nameTagIv)
            .load(pet.petProfileImg)
            .into(binding.nameTagIv)
        binding.nameTagIv.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.nameTagIv.setBackgroundResource(R.drawable.border_name_tag_img)
        binding.nameTagIv.clipToOutline = true

        binding.nameTagTv.text = pet.petName
        when(pet.petTag){
            "ANIMAL" -> {
                binding.nameTagLayout.setBackgroundResource(R.drawable.border_name_tag_animal)
                binding.registerBtn.setBackgroundResource(R.drawable.border_register_button_orange)
                this.getColor(R.color.main_color_orange)
                    .let { binding.nameTagTv.setTextColor(it) }
            }
            else -> {
                binding.nameTagLayout.setBackgroundResource(R.drawable.border_name_tag_plant)
                binding.registerBtn.setBackgroundResource(R.drawable.border_register_button_green)
                this.getColor(R.color.main_color_green)
                    .let { binding.nameTagTv.setTextColor(it) }
            }
        }
    }

    private fun initClickListener(){
        binding.writeDiaryToolbar.cancelBtn.setOnClickListener {
            backButtonPressed()
        }
        binding.registerBtn.setOnClickListener {
            if(checkIsEmpty()) {
                createDiary()
            }
        }
        binding.addImgIv.setOnClickListener {
            checkPermission()
        }
    }

    private fun backButtonPressed(){
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("기록한 내용은 저장되지 않습니다.\n그래도 나가시겠습니까?")
            .setPositiveButton("나가기",object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    finish()
                }
            })
            .setNegativeButton("취소", object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.dismiss()
                }
            })
        dialog.show()
    }

    private fun createDiary(){
        val currentTime = SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))
        val date = "${selectDateYear}-${selectDateMonth}-${selectDateDay} $currentTime"
        val tempList: ArrayList<String?> = arrayListOf(null, null, null, null, null)
        for(i in 0 until imgList.size){
            tempList[i] = imgList[i].toString()
        }
        val newDiary = Diary(
            petId = pet.petId,
            date = date,
            diaryTitle = binding.titleEdt.text.toString(),
            diaryContent = binding.contentEdt.text.toString(),
            diaryImgUrl1 = tempList[0],
            diaryImgUrl2 = tempList[1],
            diaryImgUrl3 = tempList[2],
            diaryImgUrl4 = tempList[3],
            diaryImgUrl5 = tempList[4]
        )

        val call: Call<DiaryResponse> = DiaryClient.diaryService.createDiary(newDiary)
        call.enqueue(object : Callback<DiaryResponse>{
            override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                if(response.isSuccessful){
                    val diaryResponse: DiaryResponse = response.body()!!
                    if(diaryResponse.isSuccess){
                        val intent = Intent(this@WriteDiaryActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    else {
                        showAlertDialog(diaryResponse.message)
                    }
                }
                else {
                    showAlertDialog(response.message())
                }
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
                showAlertDialog(t.message.toString())
            }
        })
    }

    private fun showAlertDialog(message: String){
        AlertDialog.Builder(this)
            .setTitle("오류 발생")
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun checkIsEmpty(): Boolean{
        if(binding.titleEdt.text.isEmpty() && binding.contentEdt.text.isEmpty()){
            Toast.makeText(this,"제목과 내용을 작성해주세요",Toast.LENGTH_SHORT).show()
        }
        else if(binding.titleEdt.text.isEmpty()){
            Toast.makeText(this,"제목을 작성해주세요",Toast.LENGTH_SHORT).show()
        }
        else if(binding.contentEdt.text.isEmpty()){
            Toast.makeText(this,"내용을 작성해주세요", Toast.LENGTH_SHORT).show()
        }
        else return true
        return false
    }

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

    override fun onPermissionGranted() {
        /**
         * 갤러리 접근
         */
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
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
        val imagePickerHeight = activityHeight / 16 * 9
        TedBottomPicker.with(this)
            .setPeekHeight(imagePickerHeight)
            .showTitle(false)
            .setCompleteButtonText("등록")
            .setSelectMaxCount(5)
            .setSpacing(10)
            .setEmptySelectionText("")
            .setSelectedForeground(R.drawable.border_selected_img)
            .setDeSelectIcon(R.drawable.ic_remove_button)
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

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this, "카메라 접근 권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun initImgRecyclerView(){
        imgListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imgListAdapter = DiaryImgRVAdapter(imgList, this)
        binding.imgRv.apply {
            layoutManager = imgListManager
            adapter = imgListAdapter
        }
        /**
         * RecyclerView item delete 구현
         */
        imgListAdapter.setMyItemClickListener(object: DiaryImgRVAdapter.MyItemClickListener{
            override fun onRemoveImg(position: Int) {
                imgListAdapter.removeItem(position)
            }
        })
    }
}