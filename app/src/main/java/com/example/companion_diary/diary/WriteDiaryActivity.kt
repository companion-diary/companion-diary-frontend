package com.example.companion_diary.diary

import android.content.Intent
import android.net.Uri
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
import org.json.JSONArray

class WriteDiaryActivity:AppCompatActivity() {

    lateinit var binding : ActivityWriteDiaryBinding
    lateinit var imgList : ArrayList<String>
    lateinit var imgListManager: LinearLayoutManager
    lateinit var imgListAdapter: DiaryImgRVAdapter
    lateinit var mIntent : Intent
    lateinit var existArr : ArrayList<Int>
    lateinit var nameTagList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteDiaryBinding.inflate(layoutInflater)
        imgList = ArrayList<String>()

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

    fun initClickListener(){
        binding.writeDiaryToolbar.cancelBtn.setOnClickListener {
            finish()
        }
        binding.writeDiaryToolbar.registerBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("${mIntent.getStringExtra("month")}", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

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
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /**
         * 사진 최대 개수 설정하기, Deprecated된 startActivityForResult 대신 registerForActivityResult 사용
         */
        if(resultCode == RESULT_OK && requestCode == 200){
            if(data?.clipData != null){
                val count = data.clipData!!.itemCount
                if(count>10){
                    Toast.makeText(this,"사진은 10장까지 선택 가능합니다",Toast.LENGTH_SHORT).show()
                    return
                }
                for(i in 0 until count){
                    val imgUri = data.clipData!!.getItemAt(i).uri
                    imgList.add(imgUri.toString())
                }
            }
            else{
                data?.data?.let { uri ->
                    val imgUri: Uri? = data?.data
                    if(imgUri != null){
                        imgList.add(imgUri.toString())
                    }
                }
            }
            imgListAdapter.notifyDataSetChanged()
        }
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