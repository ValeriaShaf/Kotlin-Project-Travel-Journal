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
    suspend fun updateItem(item: Item)

    @Query ( "SELECT * FROM items ORDER BY id ASC")
    fun getItems(): LiveData<List<Item>>

    @Query ( "SELECT * FROM items WHERE id=:id")
    suspend fun getItem(id: Int):Item


    @Query ( "DELETE FROM items")
    suspend fun deleteAll()

    @Query("SELECT * FROM items WHERE isFavorite = 1")
    fun getFavoriteItems(): LiveData<List<Item>>

}
