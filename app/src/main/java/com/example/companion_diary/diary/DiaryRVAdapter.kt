package com.example.companion_diary.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemDiaryPreviewBinding
import com.example.companion_diary.diary.entities.DiaryPreview
import com.example.companion_diary.diary.entities.DiaryPreviewList
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

/**
 * animalDiaryList data type은 Diary data class로 변경
 */
class DiaryRVAdapter(
    private var diaryPreviewList: ArrayList<DiaryPreview>,
    val mContext: Context,
): RecyclerView.Adapter<DiaryRVAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemDiaryPreviewBinding):RecyclerView.ViewHolder(binding.root){
        fun initClickListener(position: Int){
            binding.itemDiaryLayout.setOnClickListener {
                var intent = Intent(mContext,LookDiaryActivity::class.java)
                intent.putExtra("diaryList",diaryPreviewList)
                intent.putExtra("position",position)
                mContext.startActivity(intent)
            }
        }
        fun initView(diaryPreview: DiaryPreview){
            when(diaryPreview.petTag){
                "ANIMAL" -> {
//                    binding.profileIv.setImageResource(R.drawable.ic_animal)
                    binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_preview_layout_orange)
                    binding.itemDiaryNameTagTv.setTextColor(context.getColor(R.color.main_color_orange))
                    binding.itemDiaryTitleTv.setTextColor(context.getColor(R.color.main_color_orange))
                }
                "PLANT" -> {
//                    binding.profileIv.setImageResource(R.drawable.ic_plant)
                    binding.itemDiaryLayout.setBackgroundResource(R.drawable.border_diary_preview_layout_green)
                    binding.itemDiaryNameTagTv.setTextColor(context.getColor(R.color.main_color_green))
                    binding.itemDiaryTitleTv.setTextColor(context.getColor(R.color.main_color_green))
                }
            }
            binding.itemDiaryNameTagTv.text = diaryPreview.petName
            binding.itemDiaryTitleTv.text = diaryPreview.diaryTitle
            binding.itemDiaryContentsTv.text = diaryPreview.diaryContent
            binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_img)
            binding.profileIv.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.profileIv.clipToOutline = true
            Glide.with(binding.profileIv)
                .load(diaryPreview.petProfileImg)
                .into(binding.profileIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryPreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initClickListener(position)
        holder.initView(diaryPreviewList[position])
    }

    override fun getItemCount(): Int = diaryPreviewList.size
}