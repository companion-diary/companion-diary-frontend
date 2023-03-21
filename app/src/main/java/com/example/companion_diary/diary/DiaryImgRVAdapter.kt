package com.example.companion_diary.diary

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.databinding.ItemImgBinding

class DiaryImgRVAdapter(private var imgList: MutableList<Uri>, val context: Context): RecyclerView.Adapter<DiaryImgRVAdapter.ViewHolder>() {
    /**
     * img RecyclerView item delete 구현
     */

    interface MyItemClickListener{
        fun onRemoveImg(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    fun removeItem(position: Int){
        imgList.removeAt(position)
        notifyDataSetChanged()
    }

    /**
     * recyclerView Adapter 구현
     */

    var mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setImgView(position)
        holder.setClickListener(position)
    }

    override fun getItemCount(): Int = imgList.size

    inner class ViewHolder(val binding: ItemImgBinding): RecyclerView.ViewHolder(binding.root){
        fun setImgView(position: Int){
            Glide.with(mContext)
                .load(imgList[position])
                .into(binding.itemImgIv)
            binding.itemImgIv.clipToOutline = true
        }
        fun setClickListener(position: Int){
            binding.removeImgBtn.setOnClickListener { mItemClickListener.onRemoveImg(position) }
        }
    }

}