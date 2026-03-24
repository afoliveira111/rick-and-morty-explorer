package com.example.codetest.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codetest.domain.usecase.AddFavoriteUseCase
import com.example.codetest.domain.usecase.GetCharacterDetailsUseCase
import com.example.codetest.domain.usecase.IsFavoriteUseCase
import com.example.codetest.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    fun load(characterId: Int) {
        viewModelScope.launch {
            _uiState.value = CharacterDetailsUiState(isLoading = true)

            runCatching {
                val character = getCharacterDetailsUseCase(characterId)
                val isFavorite = isFavoriteUseCase(characterId)
                character to isFavorite
            }.onSuccess { (character, isFavorite) ->
                _uiState.value = CharacterDetailsUiState(
                    character = character,
                    isFavorite = isFavorite,
                    isLoading = false
                )
            }.onFailure { throwable ->
                _uiState.value = CharacterDetailsUiState(
                    isLoading = false,
                    error = throwable.message ?: "Error loading details"
                )
            }
        }
    }

    fun toggleFavorite() {
        val currentCharacter = _uiState.value.character ?: return

        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                removeFavoriteUseCase(currentCharacter.id)
                _uiState.value = _uiState.value.copy(isFavorite = false)
            } else {
                addFavoriteUseCase(currentCharacter)
                _uiState.value = _uiState.value.copy(isFavorite = true)
            }
        }
    }
}