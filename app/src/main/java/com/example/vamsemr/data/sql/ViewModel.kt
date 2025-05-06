package com.example.inventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.launch

// ViewModel pre správu hráčov
class ItemViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    // Pridanie nového hráča do databázy
    fun addItem(name: String, skore: Int, games: Int) {
        viewModelScope.launch {
            val newItem = Item(name = name, skore = skore, games = games)
            itemsRepository.insertItem(newItem)
        }
    }

    // Získanie všetkých položiek
    fun getAllItems() = itemsRepository.getAllItemsStream()

    // Odstránenie položky
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemsRepository.deleteItem(item)
        }
    }
}


class ItemViewModelFactory(private val itemsRepository: ItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ItemViewModel(itemsRepository) as T
    }
}
