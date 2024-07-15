package com.example.kotlinfinalproject.data.repository

import android.app.Application
import com.example.kotlinfinalproject.data.local_db.ItemDao
import com.example.kotlinfinalproject.data.local_db.ItemDataBase
import com.example.kotlinfinalproject.data.model.Item

class ItemRepository (application: Application){

private var itemDao:ItemDao?

init {
    val db = ItemDataBase.getDataBase(application.applicationContext)
    itemDao= db.itemsDao()
}
    fun getItems()=itemDao?.getItems()

    fun addItem(item: Item){
        itemDao?.addItem(item)
    }
    fun deleteItem(item: Item){
        itemDao?.deleteItem(item)
    }
    fun getItem(id:Int)=itemDao?.getItem(id)






}
