package com.example.kotlinfinalproject

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Item(val title:String, val description:String, val photo: String?):Parcelable

object ItemManager{
    val items: MutableList<Item> = mutableListOf()

    fun add(item: Item){
          items.add(item)

      }
    fun remove(index: Int){
        items.removeAt(index)

    }
}