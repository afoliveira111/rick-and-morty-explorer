package com.example.codetest.presentation.details

import com.example.codetest.domain.model.Character

data class CharacterDetailsUiState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
)