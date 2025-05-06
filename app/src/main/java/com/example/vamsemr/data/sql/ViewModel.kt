package com.example.inventory.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
/*
// Factory na vytvorenie ViewModelu s parametrami
class ItemViewModelFactory(private val itemsRepository: ItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemViewModel(itemsRepository) as T
    }
}
*/