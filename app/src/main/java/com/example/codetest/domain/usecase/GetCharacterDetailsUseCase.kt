package com.example.codetest.domain.usecase

import com.example.codetest.domain.repository.CharacterRepository

class GetCharacterDetailsUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) = repository.getCharacterById(id)
}