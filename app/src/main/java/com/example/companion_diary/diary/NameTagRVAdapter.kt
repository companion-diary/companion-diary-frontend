package com.example.companion_diary.diary

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.R
import com.example.companion_diary.databinding.ItemNameTagRadioButtonBinding
import com.example.companion_diary.diary.entities.Pet
import com.gun0912.tedpermission.provider.TedPermissionProvider.context

/***
 * 이름 및 내용 다시 설정, nameTagList 대신 동식물 data class 받아오기
 */

class NameTagRVAdapter(private var petList: ArrayList<Pet>): RecyclerView.Adapter<NameTagRVAdapter.ViewHolder>() {

    private var checkPosition = -1
    private var prevChecked = -1

    /**
     * 선택된 이름 태그 데이터 전달용 interface 및 함수
     */
    interface MyItemSelectedListener{
        fun onItemSelected(pet: Pet)
    }
    private lateinit var mItemSelectedListener: MyItemSelectedListener
    fun setMyItemSelectedListener(itemSelectedListener: MyItemSelectedListener){
        mItemSelectedListener = itemSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNameTagRadioButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(petList[position])
        holder.setCheckState(position)
    }

    override fun getItemCount(): Int = petList.size

    inner class ViewHolder(private val binding: ItemNameTagRadioButtonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(pet: Pet){
            /**
             * OTHER 태그인 경우
             */
            if(pet.petName == "OTHER"){
                binding.nameTagTv.text = ""
                binding.profileIv.setImageResource(0)
                binding.otherTv.visibility = View.VISIBLE
                when(pet.petTag){
                    "ANIMAL" -> {
                        binding.iconIv.setImageResource(R.drawable.ic_animal)
                        binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_other_animal_unselected)
                        binding.otherTv.setTextColor(context.getColor(R.color.main_color_orange))
                    }
                    "PLANT" -> {
                        binding.iconIv.setImageResource(R.drawable.ic_plant)
                        binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_other_plant_unselected)
                        binding.otherTv.setTextColor(context.getColor(R.color.main_color_green))
                    }
                }
                return
            }

            /**
             * OTHER 태그가 아닌 경우
             */
            binding.nameTagTv.text = pet.petName
            binding.otherTv.visibility = View.INVISIBLE
            binding.profileIv.setBackgroundResource(R.drawable.border_name_tag_img)
            binding.profileIv.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.profileIv.clipToOutline = true
            when(pet.petTag){
                "ANIMAL" -> {
                    binding.nameTagTv.setTextColor(context.getColor(R.color.main_color_orange))
                    binding.iconIv.setImageResource(R.drawable.ic_animal)
                }
                "PLANT" -> {
                    binding.nameTagTv.setTextColor(context.getColor(R.color.main_color_green))
                    binding.iconIv.setImageResource(R.drawable.ic_plant)
                }
            }
            urlToImg(pet.petProfileImg)
        }

        fun setCheckState(position: Int){
            checkChange(position)
            binding.nameTagRb.setOnClickListener {
                checkPosition = position
                mItemSelectedListener.onItemSelected(petList[position])
                notifyDataSetChanged()
            }
        }

        private fun checkChange(position: Int){
            binding.nameTagRb.isChecked = position == checkPosition
            prevChecked = position
            if(binding.nameTagRb.isChecked){
                when(petList[checkPosition].petTag){
                    "ANIMAL" -> binding.checkedBorderIv.setImageResource(R.drawable.border_name_tag_animal_selected)
                    "PLANT" -> binding.checkedBorderIv.setImageResource(R.drawable.border_name_tag_plant_selected)
                }
                binding.checkedBorderIv.visibility = View.VISIBLE
                binding.uncheckedIv.visibility = View.INVISIBLE
            }
            if(!binding.nameTagRb.isChecked){
                binding.checkedBorderIv.setImageResource(0)
                binding.checkedBorderIv.visibility = View.INVISIBLE
                binding.uncheckedIv.visibility = View.VISIBLE
            }
        }

        private fun urlToImg(imgUrl: String){
            Glide.with(binding.profileIv)
                .load(imgUrl)
                .into(binding.profileIv)
        }
    }

}