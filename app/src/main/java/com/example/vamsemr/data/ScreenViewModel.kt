package com.example.vamsemr.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScreenViewModel : ViewModel(){
    private val _stage = MutableStateFlow(1)
    val stage: StateFlow<Int> = _stage

    fun setStage(newStage: Int) {
        _stage.value = newStage
    }

}