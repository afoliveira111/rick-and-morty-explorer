package com.example.codetest.domain.usecase

import com.example.codetest.domain.repository.CharacterRepository

class RemoveFavoriteUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.removeFavorite(id)
    }
}