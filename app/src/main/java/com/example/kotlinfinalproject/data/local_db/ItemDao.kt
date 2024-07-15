package com.example.kotlinfinalproject.data.local_db


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinfinalproject.data.model.Item


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)
    @Delete
    fun deleteItem (vararg items: Item)
    @Update
    fun updateItem(item: Item)

    @Query ( "SELECT * FROM items ORDER BY title ASC")
    fun getItems(): LiveData<List<Item>>
    // TODO: instead of 'title' it should be 'content' but doesn't work

    @Query ( "SELECT * FROM items WHERE id LIKE :id")
    fun getItem(id: Int):Item
}
