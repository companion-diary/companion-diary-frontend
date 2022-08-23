package com.example.companion_diary.diary

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 작성된 일기 recyclerView 여백 설정
 */
class DiaryItemDecoration(val mContext:Context,val margin: Float): RecyclerView.ItemDecoration() {

    private fun dpToPx(context: Context,dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.resources.displayMetrics).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = dpToPx(mContext, margin)
    }
}