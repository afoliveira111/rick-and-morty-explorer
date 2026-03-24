package com.example.codetest.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codetest.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _uiState.update { it.copy(items = favorites) }
            }
        }
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onStatusSelected(status: String?) {
        _uiState.update {
            it.copy(
                selectedStatus = if (it.selectedStatus == status) null else status
            )
        }
    }

    fun onGenderSelected(gender: String?) {
        _uiState.update {
            it.copy(
                selectedGender = if (it.selectedGender == gender) null else gender
            )
        }
    }

    fun onSortSelected(sortOption: FavoriteSortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }
}