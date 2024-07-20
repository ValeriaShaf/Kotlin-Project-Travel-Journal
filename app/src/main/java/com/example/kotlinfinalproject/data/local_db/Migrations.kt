package com.example.kotlinfinalproject.data.local_db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE items ADD COLUMN date TEXT")
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add the new column with a default value
            database.execSQL("ALTER TABLE items ADD COLUMN isFavorite INTEGER DEFAULT 0")
        }
    }
}
