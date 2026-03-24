package com.example.codetest.presentation.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codetest.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadInitial()
    }

    fun loadInitial() {
        _uiState.value = CharacterListUiState(isLoading = true)
        fetchCharacters(reset = true)
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun onSearch() {
        fetchCharacters(reset = true)
    }

    fun onStatusSelected(status: String?) {
        _uiState.update { it.copy(selectedStatus = status) }
        fetchCharacters(reset = true)
    }

    fun onGenderSelected(gender: String?) {
        _uiState.update { it.copy(selectedGender = gender) }
        fetchCharacters(reset = true)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || !state.hasNextPage) return
        fetchCharacters(reset = false)
    }

    private fun fetchCharacters(reset: Boolean) {
        viewModelScope.launch {
            val state = _uiState.value
            val page = if (reset) 1 else state.currentPage

            _uiState.update {
                it.copy(
                    items = if (reset) emptyList() else it.items,
                    isLoading = true,
                    error = null,
                    currentPage = if (reset) 1 else it.currentPage,
                    hasNextPage = if (reset) true else it.hasNextPage
                )
            }

            runCatching {
                getCharactersUseCase(
                    page = page,
                    name = state.query.takeIf { it.isNotBlank() },
                    status = state.selectedStatus,
                    gender = state.selectedGender
                )
            }.onSuccess { result ->
                _uiState.update {
                    it.copy(
                        items = if (reset) result.items else it.items + result.items,
                        isLoading = false,
                        error = null,
                        currentPage = page + 1,
                        hasNextPage = result.hasNextPage
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Error loading characters"
                    )
                }
            }
        }
    }
}