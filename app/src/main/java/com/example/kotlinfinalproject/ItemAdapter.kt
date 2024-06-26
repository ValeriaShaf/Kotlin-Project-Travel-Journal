package com.example.kotlinfinalproject

import android.app.SearchManager.OnCancelListener
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.databinding.ItemLayoutBinding

class ItemAdapter (val items: List<Item>,val callBack:ItemListener):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    interface ItemListener{
        fun onItemClicked(index:Int)
        fun onItemLongClicked(index:Int)
    }
    inner class ItemViewHolder(private val binding: ItemLayoutBinding)
        :RecyclerView.ViewHolder(binding.root),OnClickListener,OnLongClickListener{
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }
        override fun onClick(v: View?) {
        callBack.onItemClicked(adapterPosition)

        }

        override fun onLongClick(v: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return false
        }

        fun bind(item: Item){

                binding.itemTitle.text=item.title
                binding.itemDescription.text=item.description
            //binding.itemImage.setImageURI(Uri.parse(item.photo.toString()))
            //TODO
            //make photo look better - new dependency in glide
            Glide.with(binding.root).load(item.photo).circleCrop().into(binding.itemImage)

        }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount()=
       items.size

}