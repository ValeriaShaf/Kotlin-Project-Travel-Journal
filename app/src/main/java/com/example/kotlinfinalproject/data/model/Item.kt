package com.example.kotlinfinalproject.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name="title")
    val title:String,
    @ColumnInfo(name="description")
    val description:String,
    @ColumnInfo(name="image")
    val photo: String?,
    //val id: String = UUID.randomUUID().toString()
    ):Parcelable{
        @IgnoredOnParcel
        @PrimaryKey(autoGenerate = true)
        var id:Int=0
    }

//object ItemManager{
//    val items: MutableList<Item> = mutableListOf()
//
//    fun add(item: Item){
//        items.add(item)
//
//
//    }
//
//fun edit(itemId: String, newItem: Item) {
//    val index = items.indexOfFirst { it.id == itemId }
//    if (index != -1) {
//        items[index] = newItem
//        // If using LiveData or any observable pattern, notify changes
//        // itemsLiveData.postValue(items)
//    }
//}
//    fun getItemById(itemId: String?): Item? {
//        return items.find { it.id == itemId }
//    }
//
//    fun remove(index: Int) {
//            items.removeAt(index)
//
//    }
//}