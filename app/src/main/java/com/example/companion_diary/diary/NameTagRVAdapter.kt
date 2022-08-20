package com.example.companion_diary.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.databinding.ItemNametagCheckboxBinding

/***
 * 이름 및 내용 다시 설정
 */

class NameTagRVAdapter(private var nameTagList: ArrayList<String>): RecyclerView.Adapter<NameTagRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNametagCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nameTagList[position])
    }

    override fun getItemCount(): Int = nameTagList.size

    inner class ViewHolder(val binding: ItemNametagCheckboxBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(nameTag:String){
            binding.nameTagTv.text = nameTag
        }
    }

}