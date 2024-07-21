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
    @ColumnInfo(name="date")
    val date: String,
    @ColumnInfo(name="location")
    val location: String,
    @ColumnInfo(name="isFavorite")
    var isFavorite: Boolean = false

):Parcelable{

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}