package com.example.companion_diary.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemDiaryPreviewBinding
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

/**
 * animalDiaryList data type은 Diary data class로 변경
 */
class DiaryRVAdater(private var DiaryList: ArrayList<String>,val mContext: Context): RecyclerView.Adapter<DiaryRVAdater.ViewHolder>() {
    inner class ViewHolder(val binding: ItemDiaryPreviewBinding):RecyclerView.ViewHolder(binding.root){
        fun initClickListener(position: Int){
            binding.itemDiaryLayout.setOnClickListener {
                var intent = Intent(mContext,LookDiaryActivity::class.java)
                /**
                 * data put
                 */
                intent.putExtra("position",position)
                mContext.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryPreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /**
         * background 변경, 현재 when문은 임시로 설정해둔 내용
         */
        holder.initClickListener(position)
        when(position%2){
            0 -> {
                holder.binding.icIv.setImageResource(R.drawable.ic_plant)
                holder.binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_layout_green)
                holder.binding.itemDiaryNameTagTv.setTextColor(context.getColor(R.color.main_color_green))
                holder.binding.itemDiaryTitleTv.setTextColor(context.getColor(R.color.main_color_green))
            }
            else -> {
                holder.binding.icIv.setImageResource(R.drawable.ic_animal)
                holder.binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_layout_orange)
                holder.binding.itemDiaryNameTagTv.setTextColor(context.getColor(R.color.main_color_orange))
                holder.binding.itemDiaryTitleTv.setTextColor(context.getColor(R.color.main_color_orange))
            }
        }


        /**
         * nameTag, title, contents textView 변경
         */
    }

    override fun getItemCount(): Int = DiaryList.size
}