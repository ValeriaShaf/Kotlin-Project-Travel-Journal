package com.example.kotlinfinalproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Item( val title:String, val description:String, val photo: String?,val id: String = UUID.randomUUID().toString()):Parcelable

object ItemManager{
    val items: MutableList<Item> = mutableListOf()

    fun add(item: Item){
        items.add(item)


    }
//    fun edit(id: String, newItem: Item) {
//
//
//        val existingItem = items.find { it.id == id }
//        if (existingItem != null) {
//            val index = items.indexOf(existingItem)
//            items[index] = newItem.copy(id = existingItem.id)
//        } else {
//            println("Item not found for id: $id")
//        }
//    }
fun edit(itemId: String, newItem: Item) {
    val index = items.indexOfFirst { it.id == itemId }
    if (index != -1) {
        items[index] = newItem
        // If using LiveData or any observable pattern, notify changes
        // itemsLiveData.postValue(items)
    }
}
    fun getItemById(itemId: String?): Item? {
        return items.find { it.id == itemId }
    }

    fun remove(index: Int) {
            items.removeAt(index)

    }
}