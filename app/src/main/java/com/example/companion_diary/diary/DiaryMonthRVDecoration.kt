package com.example.companion_diary.diary

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DiaryMonthRVDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
//        val position = parent.getChildAdapterPosition(view)
//        val count = state.itemCount
        val offset = 30

        outRect.top = offset
        outRect.bottom = offset

    }
}