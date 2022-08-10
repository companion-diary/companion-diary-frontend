package com.example.companion_diary.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemMonthBinding
import java.util.*

class DiaryMonthRVAdapter (val context: Context): RecyclerView.Adapter<DiaryMonthRVAdapter.ViewHolder>() {
    /**
     * month recyclerView Adapter -> LinearLayout
     **/

    private val mContext = context
    private val center = Int.MAX_VALUE/2
    private var calendar = Calendar.getInstance()
    private var tempMonth: Int = 0
    lateinit var dayList: MutableList<Date>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryMonthRVAdapter.ViewHolder, position: Int) {
        when(position%2){
            0-> holder.binding.itemMonthLayout.setBackgroundResource(R.drawable.border_diary_layout_orange)
            else -> holder.binding.itemMonthLayout.setBackgroundResource(R.drawable.border_diary_layout_green)
        }
        holder.setCalendar(position)
        holder.setRecyclerView()
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class ViewHolder(val binding: ItemMonthBinding): RecyclerView.ViewHolder(binding.root) {
        fun setCalendar(position: Int){
            calendar.time = Date()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.add(Calendar.MONTH,position-center)
            binding.yearMonthTv.text="${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH)+1}월"
            tempMonth = calendar.get(Calendar.MONTH)
            dayList = MutableList(6*7){ Date() }
            for(i in 0..5){
                for(j in 0..6){
                    calendar.add(Calendar.DAY_OF_MONTH,(1-calendar.get(Calendar.DAY_OF_WEEK)+j))
                    dayList[i*7+j] = calendar.time
                }
                calendar.add(Calendar.WEEK_OF_MONTH,1)
            }
        }
        fun setRecyclerView(){
            val dayListManager = GridLayoutManager(binding.root.context,7)
            val dayListAdapter = DiaryDayRVAdapter(tempMonth,dayList,mContext)
            binding.diaryDayRv.apply{
                layoutManager = dayListManager
                adapter = dayListAdapter
            }
        }
    }

}