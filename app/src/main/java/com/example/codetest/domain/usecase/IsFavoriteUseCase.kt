package com.example.codetest.domain.usecase

import com.example.codetest.domain.repository.CharacterRepository

class IsFavoriteUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}