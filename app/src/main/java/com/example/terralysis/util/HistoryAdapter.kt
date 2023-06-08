package com.example.terralysis.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.terralysis.R
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.databinding.ItemRiwayatBinding

class HistoryAdapter (
    private val onItemClickedListener : OnItemClicked,
): ListAdapter<ScanEntity, HistoryAdapter.ItemViewHolder>(DIFF_CALLBACK){

    inner class ItemViewHolder(private var binding: ItemRiwayatBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(scanDetail : ScanEntity){
            binding.tvItemName.text = scanDetail.name
            binding.tvItemDate.text = scanDetail.timestamp

            Glide.with(itemView.context)
                .load(scanDetail.uri)
                .placeholder(R.color.secondaryContainer)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivItem)

            itemView.setOnClickListener{
                onItemClickedListener.onClicked(scanDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val scanItem = getItem(position)
        if(scanItem!=null){
            holder.bind(scanItem)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ScanEntity> =
            object : DiffUtil.ItemCallback<ScanEntity>() {
                override fun areItemsTheSame(oldItem: ScanEntity, newItem: ScanEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: ScanEntity, newItem: ScanEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }

    interface OnItemClicked{
        fun onClicked(scanDetail : ScanEntity)
    }
}