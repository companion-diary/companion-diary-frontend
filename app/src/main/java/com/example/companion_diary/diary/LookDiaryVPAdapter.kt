package com.example.companion_diary.diary

import android.content.Context
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemDiaryBinding
import com.example.companion_diary.diary.entities.Diary

class LookDiaryVPAdapter(val diaryList:ArrayList<Diary>,val mContext: Context):RecyclerView.Adapter<LookDiaryVPAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root){
        fun initView(position: Int){
            binding.titleTv.setText(diaryList[position].content)
            binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_layout_orange)
            binding.contentsTv.movementMethod = ScrollingMovementMethod()
            initImgVP(position)
        }
        fun initImgVP(position: Int){
            val imgVPAdapter = LookDiaryImgVPAdapter(diaryList[position].imgList,mContext)
            binding.imgVp.apply {
                adapter = imgVPAdapter
                binding.vpDotIndicator.attachTo(binding.imgVp)
                (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            }
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