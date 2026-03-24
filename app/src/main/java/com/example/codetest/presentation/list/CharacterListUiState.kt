package com.example.codetest.presentation.list


import com.example.codetest.domain.model.Character

data class CharacterListUiState(
    val items: List<Character> = emptyList(),
    val query: String = "",
    val selectedStatus: String? = null,
    val selectedGender: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true
)