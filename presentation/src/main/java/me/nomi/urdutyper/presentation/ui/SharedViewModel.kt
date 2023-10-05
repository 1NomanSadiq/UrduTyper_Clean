package me.nomi.urdutyper.presentation.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _dataChanged = MutableStateFlow(false)
    val dataChanged get() = _dataChanged.asStateFlow()


    fun toggleDataChanged() {
        _dataChanged.value = !dataChanged.value
    }
}