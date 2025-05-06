package com.example.inventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ViewModel pre správu hráčov
class ItemViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    fun addItem(name: String, skore: Int, games: Int) {
        viewModelScope.launch {
            val id = getNextAvailableId()
            val newItem = Item(id = id, name = name, skore = skore, games = games)
            itemsRepository.insertItem(newItem)
        }
    }

    fun getItemById(id: Int): Flow<Item?> {
        return itemsRepository.getItemStream(id)
    }



    fun getAllItems() = itemsRepository.getAllItemsStream()

    suspend fun getNextAvailableId(): Int {
        val usedIds = itemsRepository.getAllIds().sorted()
        for (i in 1..(usedIds.size + 1)) {
            if (i !in usedIds) return i
        }
        return 1
    }




    // Funkcia na odstránenie položky podľa ID
    fun deleteItemById(id: Int) {
        viewModelScope.launch {
            val itemToDelete = itemsRepository.getItemStream(id).first()  // Načítame položku podľa ID
            itemToDelete?.let {
                itemsRepository.deleteItem(it)
            }
        }
    }
}


class ItemViewModelFactory(private val itemsRepository: ItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ItemViewModel(itemsRepository) as T
    }
}
