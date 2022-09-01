package com.example.companion_diary.diary

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.companion_diary.R

private const val TAG = "LookDiaryVPTransformer"

class LookDiaryVPTransformer: ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        var icon = view.findViewById<ImageView>(R.id.look_diary_icon_iv)
        var nameTag = view.findViewById<TextView>(R.id.look_diary_name_tag_tv)
        var moreBtn = view.findViewById<ImageView>(R.id.look_diary_more_iv)

        /**
         * prev, next item에서 이름태그와 더보기 버튼 안보이게 설정
         */
        view.apply {
            when {
                position <= -1f || position >= 1f -> {
                    icon.visibility = View.INVISIBLE
                    nameTag.visibility = View.INVISIBLE
                    moreBtn.visibility = View.INVISIBLE
                }
                else -> {
                    icon.visibility = View.VISIBLE
                    nameTag.visibility = View.VISIBLE
                    moreBtn.visibility = View.VISIBLE
                }
            }

        }
    }


}