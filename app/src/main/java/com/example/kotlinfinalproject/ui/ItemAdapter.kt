package com.example.kotlinfinalproject.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.databinding.ItemLayoutBinding

class ItemAdapter(
    private var items: List<Item>,
    private val listener: ItemListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemLongClicked(id: Int)
        fun onItemClicked(index: Int)
        fun onFavoriteClicked(item: Item)
    }

    inner class ItemViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                listener.onItemLongClicked(items[adapterPosition].id)
                true
            }

            binding.favoriteBtn.setOnClickListener {
                val item = items[adapterPosition]
                item.isFavorite = !item.isFavorite
                // Notify listener to update the favorite status in the ViewModel and database
                listener.onFavoriteClicked(item)
                // Update the favorite icon
                updateFavoriteIcon(item.isFavorite)
            }
        }

        fun bind(item: Item) {
            binding.itemTitle.text = item.title
            binding.itemDescription.text = item.description
            Glide.with(binding.root)
                .load(item.photo)
                .circleCrop()
                .into(binding.itemImage)

            binding.itemLocation.text = item.location
            updateFavoriteIcon(item.isFavorite)
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            val favoriteIcon = if (isFavorite) R.drawable.heart_filled else R.drawable.heart
            binding.favoriteBtn.setImageResource(favoriteIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun itemAt(position: Int) = items[position]
}
