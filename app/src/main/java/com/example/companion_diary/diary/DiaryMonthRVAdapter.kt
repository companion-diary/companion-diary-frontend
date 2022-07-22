package com.example.companion_diary.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.databinding.ItemMonthBinding
import java.util.*

class DiaryMonthRVAdapter (): RecyclerView.Adapter<DiaryMonthRVAdapter.ViewHolder>() {

    /**
     * month recyclerView Adapter -> LinearLayout
     **/

    val center = Int.MAX_VALUE/2
    private var calendar = Calendar.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryMonthRVAdapter.ViewHolder, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH,position-center)
        holder.binding.yearMonthTv.text="${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH)+1}월"
        val tempMonth = calendar.get(Calendar.MONTH)

        var dayList:MutableList<Date> = MutableList(6*7){ Date() }
        for(i in 0..5){
            for(j in 0..6){
                calendar.add(Calendar.DAY_OF_MONTH,(1-calendar.get(Calendar.DAY_OF_WEEK)+j))
                dayList[i*7+j] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH,1)
        }

        val dayListManager = GridLayoutManager(holder.binding.root.context,7)
        val dayListAdapter = DiaryDayRVAdapter(tempMonth,dayList)
        holder.binding.diaryDayRv.apply{
            layoutManager = dayListManager
            adapter = dayListAdapter
        }

    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class ViewHolder(val binding: ItemMonthBinding): RecyclerView.ViewHolder(binding.root) { }

}