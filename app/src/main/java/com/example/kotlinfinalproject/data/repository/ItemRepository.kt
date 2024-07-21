package com.example.kotlinfinalproject.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.kotlinfinalproject.data.local_db.ItemDao
import com.example.kotlinfinalproject.data.local_db.ItemDataBase
import com.example.kotlinfinalproject.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(application: Application) {

    private val itemDao: ItemDao = ItemDataBase.getDataBase(application).itemsDao()

    fun getItems(): LiveData<List<Item>> = itemDao.getItems()

    fun getFavoriteItems(): LiveData<List<Item>> = itemDao.getFavoriteItems()

    suspend fun addItem(item: Item) {
        withContext(Dispatchers.IO) {
            itemDao.addItem(item)
        }
    }

    suspend fun deleteItem(item: Item) {
        withContext(Dispatchers.IO) {
            itemDao.deleteItem(item)
        }
    }

    suspend fun getItem(id: Int): Item? {
        return withContext(Dispatchers.IO) {
            itemDao.getItem(id)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            itemDao.deleteAll()
        }
    }

    suspend fun updateItem(item: Item) {
        withContext(Dispatchers.IO) {
            itemDao.updateItem(item)
        }
    }

}
