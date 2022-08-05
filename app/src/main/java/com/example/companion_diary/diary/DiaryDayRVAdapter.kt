package com.example.companion_diary.diary

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.DialogWriteDiaryBinding
import com.example.companion_diary.databinding.ItemDayBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import java.util.*

class DiaryDayRVAdapter (val tempMonth:Int, val dayList:MutableList<Date>,val context: Context): RecyclerView.Adapter<DiaryDayRVAdapter.ViewHolder>() {
    /**
     * day recyclerView Adapter -> GridLayout
     **/

    private val mContext = context
    private val ROW = 6
    private lateinit var existArr : ArrayList<Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDiaryExistMark(position)
        holder.setView(position)
        holder.setClickListener(position)
    }

    override fun getItemCount(): Int = ROW * 7

    inner class ViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setView(position: Int){
            binding.itemDayTv.text = dayList[position].date.toString()
            binding.itemDayTv.setTextColor(
                when (position % 7) {
                    0 -> ContextCompat.getColor(context, R.color.main_color_orange)
                    6 -> ContextCompat.getColor(context, R.color.main_color_green)
                    else -> Color.BLACK
                }
            )
            if (tempMonth != dayList[position].month) {
                binding.itemDayTv.alpha = 0f
                binding.diaryExistIv.alpha = 0f
                binding.itemDayTv.isEnabled = false
            }
        }
        fun setClickListener(position: Int){
            binding.itemDayTv.setOnClickListener {
                initBottomSheetDialog(position)
            }
        }
        fun setDiaryExistMark(position: Int){
            /**
             * 서버 연결 전 해당 날짜의 일기가 있다는 표시를 화면에 미리 띄워주는 역할
             */
            val sharedPreferences = mContext.getSharedPreferences("${dayList[position].month + 1}",MODE_PRIVATE)
            existArr = ArrayList<Int>()
            if(sharedPreferences.contains("existArr")){
                var jsonArr = JSONArray(sharedPreferences.getString("existArr",""))
                for(i in 0 until jsonArr.length()){
                    existArr.add(jsonArr.optInt(i))
                }
            }
            for(i in 0 until existArr.size){
                if(existArr[i] == position){
                    binding.diaryExistIv.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun dayOfTheWeek(position: Int): String {
        return when (position % 7) {
            0 -> "일"
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            else -> "토"
        }
    }
    private fun initBottomSheetDialog(position: Int){
        var dateStr = "${dayList[position].year % 100 + 2000}년 ${dayList[position].month + 1}월 ${dayList[position].date}일"
        val dialog = BottomSheetDialog(mContext)
        dialog.setContentView(R.layout.dialog_write_diary)
        val dateTv = dialog.findViewById<TextView>(R.id.year_month_date_tv)
        val prevBtn = dialog.findViewById<ImageView>(R.id.prev_iv)
        val writeDiaryBtn = dialog.findViewById<Button>(R.id.write_diary_btn)
        val companionRv = dialog.findViewById<RecyclerView>(R.id.companion_name_tag_rv)
        var nameTagList = ArrayList<String>()

        nameTagList.apply{
            add("호두")
            add("제임스")
        }

        var nameTagRVAdpater = DiaryNameTagRVAdapter(nameTagList)
        var nameTagLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        companionRv?.apply{
            adapter = nameTagRVAdpater
            layoutManager = nameTagLayoutManager
        }

        dateTv?.text = dateStr
        prevBtn?.setOnClickListener {
            dialog.dismiss()
        }
        writeDiaryBtn?.setOnClickListener {
            var intent = Intent(mContext, WriteDiaryActivity::class.java)
            intent.putExtra(
                "dateTitle",
                dateStr + "${dayOfTheWeek(position)}요일"
            )
            intent.putExtra("month","${dayList[position].month + 1}")
            intent.putExtra("position",position)
            intent.putExtra("nameTagList",nameTagList)
            mContext.startActivity(intent)
            dialog.dismiss()
        }
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        dialog.show()
    }
}