package com.example.kotlinfinalproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.databinding.ItemLayoutBinding

class ItemAdapter(private val items: List<Item>, private val listener: ItemListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(id: Int )
        fun onFavoriteClicked(item: Item)
    }

    inner class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
            binding.favoriteBtn.setOnClickListener {
                val item = items[adapterPosition]
                listener.onFavoriteClicked(item) // Notify the click event
            }
        }

        override fun onClick(v: View?){
            listener.onItemClicked(adapterPosition)

        }

        override fun onLongClick(v: View?): Boolean {
            listener.onItemLongClicked(items[adapterPosition].id)
            return true
        }

        fun bind(item: Item) {
            binding.itemTitle.text = item.title
            binding.itemDescription.text = item.description
            Glide.with(binding.root)
                .load(item.photo)
                .circleCrop()
                .into(binding.itemImage)

            val favoriteIcon = if (item.isFavorite) R.drawable.heart_filled else R.drawable.heart
            binding.favoriteBtn.setImageResource(favoriteIcon)
        }
    }

    fun itemAt(position: Int)=items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}