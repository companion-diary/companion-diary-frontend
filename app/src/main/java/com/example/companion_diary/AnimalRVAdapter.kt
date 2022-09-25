package com.example.companion_diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companion_diary.databinding.ItemAnimalBinding

class AnimalRVAdapter(private var animalList: List<Animal>): RecyclerView.Adapter<AnimalRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AnimalRVAdapter.ViewHolder {
        val binding: ItemAnimalBinding = ItemAnimalBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }
    //데이터를 바인딩 해 줄 작업
    override fun onBindViewHolder(holder: AnimalRVAdapter.ViewHolder, position: Int) {
        holder.bind(animalList[position])
    }

    override fun getItemCount(): Int = animalList.size

    inner class ViewHolder(val binding: ItemAnimalBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(animal: Animal){
            binding.itemAnimalNameTv.text=animal.name
            binding.itemAnimalSpeciesTv.text=animal.species
            binding.itemAnimalAgeTv.text=animal.age
            binding.itemAnimalGenderIv.setImageResource(R.drawable.gender_animal)
            Glide.with(itemView).load(animal.animalImg).into(binding.itemAnimalIv)
        }
    }
}