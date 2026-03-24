package com.example.codetest.domain.usecase

import com.example.codetest.domain.model.Character
import com.example.codetest.domain.repository.CharacterRepository

class AddFavoriteUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(character: Character) {
        repository.addFavorite(character)
    }
}