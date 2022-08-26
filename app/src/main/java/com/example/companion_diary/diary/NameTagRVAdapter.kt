package com.example.companion_diary.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemNameTagRadioButtonBinding
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

/***
 * 이름 및 내용 다시 설정, nameTagList 대신 동식물 data class 받아오기
 */

class NameTagRVAdapter(private var nameTagList: ArrayList<String>): RecyclerView.Adapter<NameTagRVAdapter.ViewHolder>() {

    private var checkPosition = -1

    /**
     * 선택된 이름 태그 데이터 전달용 interface 및 함수
     */
    interface MyItemSelectedListener{
        fun onItemSelected(nameTag: String)
    }
    private lateinit var mItemSelectedListener: MyItemSelectedListener
    fun setMyItemSelectedListener(itemSelectedListener: NameTagRVAdapter.MyItemSelectedListener){
        mItemSelectedListener = itemSelectedListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNameTagRadioButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.setCheckState(position)
    }

    override fun getItemCount(): Int = nameTagList.size

    inner class ViewHolder(val binding: ItemNameTagRadioButtonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.nameTagTv.text = nameTagList[position]
            /**
             * 동물인지 식물인지 확인 후 색상 변경
             */
            when(position%2){
                0 -> {
                    binding.nameTagTv.setTextColor(context.getColor(R.color.main_color_orange))
                    binding.checkedBorderIv.setImageResource(R.drawable.border_name_tag_checked_animal)
                    binding.iconIv.setImageResource(R.drawable.ic_animal)
                    binding.profileIv.setImageResource(R.drawable.img) // profile img
                    binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_img)
                    binding.profileIv.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.profileIv.clipToOutline = true
                }
                1 -> {
                    binding.nameTagTv.setTextColor(context.getColor(R.color.main_color_green))
                    binding.checkedBorderIv.setImageResource(R.drawable.border_name_tag_checked_plant)
                    binding.iconIv.setImageResource(R.drawable.ic_plant)
                    binding.profileIv.setImageResource(R.drawable.img_1) // profile img
                    binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_img)
                    binding.profileIv.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.profileIv.clipToOutline = true
                }
            }
        }
        fun setCheckState(position: Int){
            binding.nameTagRb.isChecked = position == checkPosition
            if(binding.nameTagRb.isChecked){
                binding.checkedBorderIv.visibility = View.VISIBLE
                binding.uncheckedIv.visibility = View.INVISIBLE
            }
            if(!binding.nameTagRb.isChecked){
                binding.checkedBorderIv.visibility = View.INVISIBLE
                binding.uncheckedIv.visibility = View.VISIBLE
            }
            binding.nameTagRb.setOnClickListener {
                checkPosition = position
                mItemSelectedListener.onItemSelected(nameTagList[position])
                notifyDataSetChanged()
            }
        }
    }

}