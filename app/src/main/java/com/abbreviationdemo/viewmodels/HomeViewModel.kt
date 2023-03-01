package com.abbreviationdemo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abbreviationdemo.models.AbbreviationManager
import com.abbreviationdemo.models.FullForms
import com.abbreviationdemo.models.UIState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(private val abbreviationManager: AbbreviationManager) : ViewModel() {

    private val fullFormListUIState = MutableLiveData<UIState<List<FullForms>>>()

    fun getFullFormListUiState(): LiveData<UIState<List<FullForms>>> = fullFormListUIState

    fun getFullForms(abbreviation: String) {
        viewModelScope.launch {
            try {
                coroutineScope {
                    val list = abbreviationManager.getFullForms(abbreviation)
                    fullFormListUIState.postValue(UIState.Success(list))
                }
            } catch (e: java.lang.Exception) {
                fullFormListUIState.postValue(UIState.Error(e.localizedMessage.orEmpty()))
            }
        }
    }
}

