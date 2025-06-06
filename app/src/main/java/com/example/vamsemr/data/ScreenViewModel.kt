package com.example.vamsemr.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Stage hodnoty, ako pomocna premenna na zobrazenie spravneho screenu
 *
 * @author Bc. Fabo Peter
 */
class ScreenViewModel : ViewModel(){
    private val _stage = MutableStateFlow(1)
    val stage: StateFlow<Int> = _stage

    /**
     * Zmen hodnotu stage
     */
    fun setStage(newStage: Int) {
        _stage.value = newStage
    }

}