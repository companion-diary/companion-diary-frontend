package com.example.companion_diary.diary

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.databinding.ItemDayBinding
import java.util.*

class DiaryDayRVAdapter (val tempMonth:Int, val dayList:MutableList<Date>): RecyclerView.Adapter<DiaryDayRVAdapter.ViewHolder>() {

    /**
     * day recyclerView Adapter -> GridLayout
     **/

    val ROW = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemDayTv.setOnClickListener {
            Log.d("DayRVAdapter","${dayList[position].year%100 +2000}년 ${dayList[position].month +1}월 ${dayList[position].date}일 클릭")
        }
        holder.binding.itemDayTv.text=dayList[position].date.toString()
        holder.binding.itemDayTv.setTextColor(
            when(position%7){
                0 -> Color.RED
                6 -> Color.BLUE
                else -> Color.BLACK
            }
        )
        if(tempMonth != dayList[position].month){
            holder.binding.itemDayTv.alpha = 0.25f
        }
    }

    override fun getItemCount(): Int = ROW * 7

    inner class ViewHolder(val binding: ItemDayBinding): RecyclerView.ViewHolder(binding.root) { }
}