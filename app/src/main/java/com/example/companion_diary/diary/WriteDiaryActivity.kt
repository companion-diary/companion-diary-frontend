package com.example.companion_diary.diary

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.MainActivity
import com.example.companion_diary.databinding.ActivityMainBinding
import com.example.companion_diary.databinding.ActivityWriteDiaryBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import org.json.JSONArray

class WriteDiaryActivity:AppCompatActivity(), PermissionListener {

    lateinit var binding : ActivityWriteDiaryBinding
    lateinit var imgList : MutableList<Uri>
    lateinit var imgListManager: LinearLayoutManager
    lateinit var imgListAdapter: DiaryImgRVAdapter
    lateinit var mIntent : Intent
    lateinit var existArr : ArrayList<Int>
    lateinit var nameTagList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteDiaryBinding.inflate(layoutInflater)
        imgList = ArrayList<Uri>()

        initView()
        initClickListener()
        initImgRecyclerView()

        /**
         *  nameTagList : string -> 등록된 동식물 객체로 변경
         *  bottomSheetDialog에서 선택한 동식물 태그만 넘어오도록 설정
         */

        /**
         * 갤러리에서 이미지 받아와서 imgList에 추가하도록 설정
         */

        setContentView(binding.root)
    }

    fun initView(){
        mIntent = intent
        var dateTitle = mIntent.getStringExtra("year")+"년"+mIntent.getStringExtra("monthOfYear")+"월"+mIntent.getStringExtra("dayOfMonth")+"일"
        //nameTagList = mIntent.getStringArrayListExtra("nameTagList") as ArrayList<String>
        //initNameTagRecyclerView(nameTagList)
        binding.yearMonthDateTv.text = dateTitle
    }

    private fun initClickListener(){
        binding.writeDiaryToolbar.cancelBtn.setOnClickListener {
            finish()
        }
        binding.writeDiaryToolbar.registerBtn.setOnClickListener {
//            val sharedPreferences = getSharedPreferences("${mIntent.getStringExtra("month")}", MODE_PRIVATE)
//            val editor = sharedPreferences.edit()

//            /**
//             * SharedPreferences에 저장되어있는 existArr 가져와서 등록 버튼 누른 날짜를 추가
//             */
//            existArr = ArrayList<Int>()
//            if(sharedPreferences.contains("existArr")){
//                var existString = sharedPreferences.getString("existArr","")
//                var getJsonArr = JSONArray(existString)
//                for(i in 0 until getJsonArr.length()){
//                    existArr.add(getJsonArr.optInt(i))
//                }
//            }
//            existArr.add(mIntent.getIntExtra("position",0))
//
//            /**
//             * existArr을 Json배열로 변경해서 String으로 변경 후 SharedPreferences에 저장
//             */
//            var setJsonArr = JSONArray()
//            for(i in existArr){
//                setJsonArr.put(i)
//            }
//            editor.putString("existArr", setJsonArr.toString())
//            editor.apply()

            /**
             * 화면 전환
             */
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.addImgTv.setOnClickListener {
            checkPermission()
        }
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23) { // 마시멜로우 이상 버전
            TedPermission.create()
                .setPermissionListener(this)
                .setRationaleMessage("카메라 접근 허용이 필요한 서비스입니다.")
                .setDeniedMessage("[설정] → [권한]에서 권한 변경이 가능합니다.")
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
        TedBottomPicker.with(this)
            .setPeekHeight(1600)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .setSelectedUriList(imgList)
            .showMultiImage(object: TedBottomSheetDialogFragment.OnMultiImageSelectedListener{
                override fun onImagesSelected(uriList: MutableList<Uri>?) {
                    if(uriList == null) return
                    imgList.clear()
                    imgList.addAll(uriList!!)
                    imgListAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this, "카메라 접근 권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
    }

    fun initImgRecyclerView(){
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

    fun initNameTagRecyclerView(nameTagList: ArrayList<String>){
        val nameTagListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val nameTagListAdapter = DiaryWriteNameTagRVAdapter(nameTagList)
        binding.writeDiaryActNameTagRv.apply {
            layoutManager = nameTagListManager
            adapter = nameTagListAdapter
        }
    }

}