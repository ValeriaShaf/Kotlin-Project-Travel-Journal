package com.example.kotlinfinalproject.data.local_db


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinfinalproject.data.model.Item


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)
    @Delete
    suspend fun deleteItem (vararg items: Item)
    @Update
    fun updateItem(item: Item)

    @Query ( "SELECT * FROM items ORDER BY title ASC")
    fun getItems(): LiveData<List<Item>>
    // TODO: instead of 'title' it should be 'content' but doesn't work

    @Query ( "SELECT * FROM items WHERE id=:id")
    suspend fun getItem(id: Int):Item

    @Query ( "DELETE FROM items")
    suspend fun deleteAll()
}
