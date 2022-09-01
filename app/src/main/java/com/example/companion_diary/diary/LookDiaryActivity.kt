package com.example.companion_diary.diary

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.databinding.ActivityLookDiaryBinding
import com.example.companion_diary.diary.entities.Diary

class LookDiaryActivity:AppCompatActivity() {
    lateinit var binding: ActivityLookDiaryBinding
    lateinit var diaryList: ArrayList<Diary>
    lateinit var mIntent : Intent
    val TAG = "LookDiaryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLookDiaryBinding.inflate(layoutInflater)

        mIntent = intent
        getDiaryList()
        initVp()
        initView()
        setContentView(binding.root)
    }

    private fun getDiaryList(){
        diaryList = ArrayList<Diary>()
        /**
         * intent로 데이터 받아오기
         * 아래는 dummy data
         */
        diaryList.apply{
            add(Diary("2022.08.22","호두야 안녕","호두 귀여워","호두"))
            add(Diary("2022.08.22","호두야 안녕","호두 귀여워1","호두"))
            add(Diary("2022.08.22","호두야 안녕","호두 귀여워2","호두"))
            add(Diary("2022.08.22","호두야 안녕","호두 귀여워3","호두"))
        }
    }

    fun initView(){
        binding.prevIv.bringToFront()
        binding.prevItemIv.bringToFront()
        binding.nextItemIv.bringToFront()
        /**
         * diary 내용 view에 보여주기
         */
        setClickListener()
    }

    fun setClickListener(){
        binding.prevIv.setOnClickListener {
            finish()
        }
        binding.prevItemIv.setOnClickListener {
            binding.lookDiaryVp.currentItem -= 1
        }
        binding.nextItemIv.setOnClickListener {
            binding.lookDiaryVp.currentItem += 1
        }
    }

    fun initVp(){
        val lookDiaryAdapter = LookDiaryVPAdapter(diaryList)
        val pos = mIntent.getIntExtra("position",0)
        binding.lookDiaryVp.apply{
            adapter = lookDiaryAdapter
            (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setCurrentItem(pos,false)

            /**
             * pageTransformer 설정
             */
            var compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(50))
            compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
                var r = 1 - Math.abs(position)
                page.scaleX = 0.9f + r * 0.1f
            })
            compositePageTransformer.addTransformer(LookDiaryVPTransformer())
            setPageTransformer(compositePageTransformer)

            /**
             * prev, next item padding 설정
             */
            var itemPadding = 180
            setPadding(0,itemPadding,0,itemPadding)

            /**
             * 스크롤 막기
             */
            isUserInputEnabled = false
        }
    }

}