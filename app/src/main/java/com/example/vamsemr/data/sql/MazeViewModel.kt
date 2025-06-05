package com.example.vamsemr.data.sql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.inventory.data.MazesRepository
import com.example.inventory.data.compMazes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// Kód prevzatý a následne upravený zo cvičení VAPMZ.

class MazeViewModel(private val mazesRepository: MazesRepository) : ViewModel() {

    fun addMaze(maze: compMazes) {
        viewModelScope.launch {
            mazesRepository.insertMaze(maze)
        }
    }

    fun addOrUpdateMaze(maze: compMazes) {
        viewModelScope.launch {
            val existing = mazesRepository.getMazeStream(maze.id).first()
            if (existing == null) {
                mazesRepository.insertMaze(maze)
            } else {
                mazesRepository.updateMaze(maze)
            }
        }
    }
/*
    fun getMazeById(id: Int): Flow<compMazes?> {
        return mazesRepository.getMazeStream(id)
    }
*/
    suspend fun getMazeById(id: Int): compMazes? {
        return mazesRepository.getMazeStream(id).firstOrNull()
    }


    fun updateMaze(maze: compMazes) {
        viewModelScope.launch {
            mazesRepository.updateMaze(maze)
        }
    }

    fun deleteMaze(maze: compMazes) {
        viewModelScope.launch {
            mazesRepository.deleteMaze(maze)
        }
    }

    fun getAllMazes() = mazesRepository.getAllMazesStream()
}

class MazeViewModelFactory(private val mazesRepository: MazesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MazeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MazeViewModel(mazesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
