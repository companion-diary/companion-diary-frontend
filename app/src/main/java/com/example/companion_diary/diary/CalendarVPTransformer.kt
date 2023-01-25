package com.example.companion_diary.diary

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.R
import com.example.companion_diary.databinding.FragmentCalendarBinding
import java.util.*
import kotlin.math.abs

class CalendarVPTransformer(): ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {

        val layout = view.findViewById<ConstraintLayout>(R.id.item_month_layout)
        val monthTv = view.findViewById<TextView>(R.id.month_tv)

        view.apply {
            /**
             * margin 설정
             * scaleY가 변경되면서 다음 아이템 찌그러지는 현상 발생
             * -> 삭제
             */
//            var v = 1- abs(position)
//            view.scaleY = 0.85f + v * 0.15f

            /**
             * month 계산
             */
            var month = monthTv.text.substring(0 until monthTv.text.length-1).toInt()

            /**
             * alpha 값, background 설정
             */
            when (position) {
                0f -> {
                    view.alpha = 1f
                    when(month%2){
                        0 -> layout.setBackgroundResource(R.drawable.border_diary_layout_orange_selected)
                        else -> layout.setBackgroundResource(R.drawable.border_diary_layout_green_selected)
                    }
                }
                else -> {
                    view.alpha = 0.6f
                    layout.setBackgroundResource(R.drawable.border_diary_layout_unselected)
                }
            }
        }

    }
}