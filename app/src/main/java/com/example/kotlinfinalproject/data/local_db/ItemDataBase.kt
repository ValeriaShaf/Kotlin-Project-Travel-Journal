package com.example.kotlinfinalproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.example.kotlinfinalproject.data.model.Item

@Database(entities = [Item::class], version = 4, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemsDao(): ItemDao

    companion object {

        @Volatile
        private var instance: ItemDataBase? = null

        fun getDataBase(context: Context): ItemDataBase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "items_db"
                )
                    .addMigrations(Migrations.MIGRATION_1_2, Migrations.MIGRATION_2_3, Migrations.MIGRATION_3_4) // Add migration here
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}
