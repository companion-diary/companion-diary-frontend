package com.example.companion_diary.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.databinding.ItemNametagBinding

class DiaryWriteNameTagRVAdapter(private var nameTagList: ArrayList<String>): RecyclerView.Adapter<DiaryWriteNameTagRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNametagBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nameTagList[position])
    }

    override fun getItemCount(): Int = nameTagList.size

    inner class ViewHolder(val binding:ItemNametagBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(nameTag: String){
            binding.nameTagTv.text = nameTag
        }
    }
}