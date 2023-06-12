package com.example.terralysis.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.terralysis.databinding.ItemCarouselBinding

class CarouselAdapter (
    private val imageList : List<Int>
    ): RecyclerView.Adapter<CarouselAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private var binding: ItemCarouselBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(image : Int){
            binding.ivCarousel.setImageDrawable(AppCompatResources.getDrawable(itemView.context, image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val image = imageList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}