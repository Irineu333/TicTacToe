package com.neo.hash.ui.screen.mainScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.hash.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val referenceCode get() = dataStoreRepository.flow

    fun setReferenceCode(code: String) = viewModelScope.launch {
        dataStoreRepository.setReferenceCode(code)
    }

    fun clearReferenceCode() = viewModelScope.launch {
        dataStoreRepository.setReferenceCode("")
    }
}
