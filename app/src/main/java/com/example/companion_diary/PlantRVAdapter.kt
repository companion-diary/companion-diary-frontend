package com.example.companion_diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.databinding.ItemPlantBinding

class PlantRVAdapter(private var plantList: ArrayList<Plant>): RecyclerView.Adapter<PlantRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlantRVAdapter.ViewHolder {
        val binding: ItemPlantBinding = ItemPlantBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }
    //데이터를 바인딩 해 줄 작업
    override fun onBindViewHolder(holder: PlantRVAdapter.ViewHolder, position: Int) {
        holder.bind(plantList[position])
    }

    override fun getItemCount(): Int = plantList.size

    inner class ViewHolder(val binding: ItemPlantBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(plant: Plant){
            binding.itemPlantNameTv.text=plant.name
            binding.itemPlantSpeciesTv.text=plant.species
            binding.itemPlantAgeTv.text=plant.age
            Glide.with(itemView).load(plant.plantImg).into(binding.itemPlantIv)
        }
    }
}