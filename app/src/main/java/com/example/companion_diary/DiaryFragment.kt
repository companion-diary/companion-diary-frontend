package com.example.companion_diary

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.databinding.FragmentDiaryBinding
import com.example.companion_diary.diary.CalendarVPAdapter
import com.example.companion_diary.diary.CalendarVPTransformer
import com.example.companion_diary.diary.entities.Date
import org.joda.time.DateTime

class DiaryFragment : Fragment() {

    lateinit var binding: FragmentDiaryBinding
    val TAG = "DiaryFragment"
    private var today = DateTime()
    private var pos = Int.MAX_VALUE/2

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
//        binding.explanationTv.typeface = resources.getFont(R.font.jalnan)
        val exStr = "동식이 일기"
        var spStr = SpannableString(exStr)
        spStr.setSpan(
            context?.getColor(R.color.main_color_orange)?.let { ForegroundColorSpan(it) }, 0, 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spStr.setSpan(
            context?.getColor(R.color.main_color_green)?.let { ForegroundColorSpan(it) }, 1, 2,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spStr.setSpan(
            context?.getColor(R.color.main_color_black)?.let { ForegroundColorSpan(it) },
            2,
            exStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.explanationTv.text = spStr
    }

    /**
     * viewPager 설정
     **/
    private fun initVp() {
        val calendarAdapter = CalendarVPAdapter(context as MainActivity)
        binding.calendar.apply {
            adapter = calendarAdapter
            setCurrentItem(CalendarVPAdapter.START_POSITION,false)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2

            val vpHeight = resources.displayMetrics.heightPixels - resources.getDimension(R.dimen.diary_calendar_top_space).toInt()
            val marginBottom = resources.getDimension(R.dimen.diary_calendar_margin_bottom).toInt()
            val diaryHeight = resources.getDimension(R.dimen.diary_calendar_height).toInt()

            /**
             * margin 설정, animation 설정
             */
            val transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(120))
            transform.addTransformer(CalendarVPTransformer())
            setPageTransformer(transform)

            /**
             * padding 설정
             */

            val bottomPadding = ((vpHeight-marginBottom-diaryHeight)*0.9).toInt()
//            val bottomPadding = 500
            setPadding(0,0,0,bottomPadding)
        }
    }
}