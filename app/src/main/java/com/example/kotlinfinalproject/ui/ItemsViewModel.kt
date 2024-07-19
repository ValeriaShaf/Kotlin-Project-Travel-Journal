package com.example.kotlinfinalproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.data.repository.ItemRepository
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ItemRepository(application)

    val items: LiveData<List<Item>>? = repository.getItems()

    private  val _chosenItem = MutableLiveData<Item>()
    val chosenItem: LiveData<Item> get() = _chosenItem

    fun setItem(item: Item){
        _chosenItem.value = item
    }
    fun addItem(item: Item){
        viewModelScope.launch{
            repository.addItem(item)}

    }

    fun deleteItem(item: Item){
        viewModelScope.launch{
        repository.deleteItem(item)}
    }

    fun deleteAll(){
        viewModelScope.launch{
        repository.deleteAll()}
    }
}