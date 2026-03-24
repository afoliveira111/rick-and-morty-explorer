package com.example.codetest.domain.usecase


import com.example.codetest.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ) = repository.getCharacters(
        page = page,
        name = name,
        status = status,
        gender = gender
    )
}