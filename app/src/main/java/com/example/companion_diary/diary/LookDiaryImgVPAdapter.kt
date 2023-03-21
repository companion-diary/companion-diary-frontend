package com.example.companion_diary.diary

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.databinding.ItemDiaryImgBinding

class LookDiaryImgVPAdapter(private val imgList: ArrayList<String?>,val mContext: Context): RecyclerView.Adapter<LookDiaryImgVPAdapter.ViewHolder>() {
    lateinit var binding :ItemDiaryImgBinding
    inner class ViewHolder(binding:ItemDiaryImgBinding):RecyclerView.ViewHolder(binding.root){
        fun setImgView(position: Int){
            if(imgList.isNotEmpty()){
                Glide.with(mContext)
                    .load(imgList!![position])
                    .into(binding.itemDiaryImgIv)
                binding.itemDiaryImgIv.clipToOutline = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDiaryImgBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setImgView(position)
    }

    override fun getItemCount(): Int = imgList.size
}