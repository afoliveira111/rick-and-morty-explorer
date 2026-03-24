package com.example.codetest.presentation.favorites

import com.example.codetest.domain.model.Character

enum class FavoriteSortOption {
    NAME_ASC,
    NAME_DESC
}

data class FavoritesUiState(
    val items: List<Character> = emptyList(),
    val query: String = "",
    val selectedStatus: String? = null,
    val selectedGender: String? = null,
    val sortOption: FavoriteSortOption = FavoriteSortOption.NAME_ASC
)