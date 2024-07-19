package com.example.kotlinfinalproject.data.repository

import android.app.Application
import com.example.kotlinfinalproject.data.local_db.ItemDao
import com.example.kotlinfinalproject.data.local_db.ItemDataBase
import com.example.kotlinfinalproject.data.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ItemRepository (application: Application) : CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private var itemDao:ItemDao?

init {
    val db = ItemDataBase.getDataBase(application.applicationContext)
    itemDao= db.itemsDao()
}
    fun getItems() = itemDao?.getItems()

    suspend fun addItem(item: Item) =  itemDao?.addItem(item)
    suspend fun deleteItem(item: Item) = itemDao?.deleteItem(item)

    suspend fun getItem(id:Int)=itemDao?.getItem(id)

    suspend fun deleteAll(){
        itemDao?.deleteAll()
    }






}
