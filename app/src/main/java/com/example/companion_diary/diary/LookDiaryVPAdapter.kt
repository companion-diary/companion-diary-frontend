package com.example.companion_diary.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemDiaryBinding
import com.example.companion_diary.diary.entities.Diary

class LookDiaryVPAdapter(val diaryList:ArrayList<Diary>):RecyclerView.Adapter<LookDiaryVPAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun initView(position: Int){
            binding.titleTv.setText(diaryList[position].content)
            binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_layout_orange)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(position)
    }

    override fun getItemCount(): Int = diaryList.size

}