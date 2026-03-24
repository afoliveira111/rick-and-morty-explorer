package com.example.codetest.domain.usecase

import com.example.codetest.domain.repository.CharacterRepository

class GetFavoritesUseCase(
    private val repository: CharacterRepository
) {
    operator fun invoke() = repository.getFavoriteCharacters()
}