package com.example.companion_diary.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.MainActivity
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
        return binding.root
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
             * margin 설정
             */
            var transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(1))
            transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
                var v = 1-Math.abs(fl)
                view.scaleY = 0.9f + v * 0.1f
            })
            setPageTransformer(transform)

            /**
             * padding 설정
             */
            setPadding(0,0,0,280)
        }
    }

}