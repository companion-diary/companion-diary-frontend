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
             */
            MarginPageTransformer(1)
            var v = 1- abs(position)
            view.scaleY = 0.9f + v * 0.1f

            /**
             * month 계산
             */
            var month = monthTv.text.substring(0 until monthTv.text.length-1).toInt()

            /**
             * alpha 값, background 설정
             */
            when {
                position == 0f -> {
                    view.alpha = 1f
                    when(month%2){
                        0 -> layout.setBackgroundResource(R.drawable.border_diary_layout_orange_selected)
                        else -> layout.setBackgroundResource(R.drawable.border_diary_layout_green_selected)
                    }
                }
                else -> {
                    view.alpha = 0.5f
                    when(month%2){
                        0 -> layout.setBackgroundResource(R.drawable.border_diary_layout_orange)
                        else -> layout.setBackgroundResource(R.drawable.border_diary_layout_green)
                    }
                }
            }
        }

    }
}