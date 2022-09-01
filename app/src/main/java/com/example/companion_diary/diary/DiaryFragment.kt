package com.example.companion_diary.diary

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companion_diary.MainActivity
import com.example.companion_diary.R
import com.example.companion_diary.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    lateinit var binding: FragmentDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        initVp()
        initTv()
        return binding.root
    }

    fun initTv() {
        val exStr = "동식이와 함께\n" + "오늘을 기억해요!"
        var spStr = SpannableString(exStr)
        spStr.setSpan(context?.getColor(R.color.main_color_orange)?.let { ForegroundColorSpan(it) },0,1,SPAN_EXCLUSIVE_EXCLUSIVE)
        spStr.setSpan(context?.getColor(R.color.main_color_green)?.let { ForegroundColorSpan(it) },1,2,SPAN_EXCLUSIVE_EXCLUSIVE)
        spStr.setSpan(context?.getColor(R.color.main_color_black)?.let { ForegroundColorSpan(it) }, 2, exStr.length,SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.explanationTv.text = spStr
    }

    /**
     * viewPager 설정
     **/
    fun initVp() {
        val calendarAdapter = CalendarVPAdapter(context as MainActivity)
        binding.calendar.apply {
            adapter = calendarAdapter
            setCurrentItem(CalendarVPAdapter.START_POSITION,false)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2

            /**
             * margin 설정, animation 설정
             */
            setPageTransformer(CalendarVPTransformer())

            /**
             * padding 설정
             */
            setPadding(0,0,0,280)
        }
    }

}