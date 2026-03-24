package com.example.codetest.domain.repository


import com.example.codetest.domain.model.Character
import com.example.codetest.domain.model.PagedCharacters
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ): PagedCharacters

    suspend fun getCharacterById(id: Int): Character

    fun getFavoriteCharacters(): Flow<List<Character>>

    fun observeFavoriteIds(): Flow<Set<Int>>

    suspend fun addFavorite(character: Character)

    suspend fun removeFavorite(characterId: Int)

    suspend fun isFavorite(characterId: Int): Boolean
}