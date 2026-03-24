package com.example.codetest.data.repository

import com.example.codetest.data.local.FavoriteCharacterDao
import com.example.codetest.data.mapper.toDomain
import com.example.codetest.data.mapper.toEntity
import com.example.codetest.data.remote.CharacterApiService
import com.example.codetest.domain.model.Character
import com.example.codetest.domain.model.PagedCharacters
import com.example.codetest.domain.repository.CharacterRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val api: CharacterApiService,
    private val dao: FavoriteCharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ): PagedCharacters {
        val response = api.getCharacters(
            page = page,
            name = name,
            status = status,
            gender = gender
        )

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = api.getCharacterResponseBody(response)
                PagedCharacters(
                    items = body.results.map { it.toDomain() },
                    hasNextPage = body.info.next != null
                )
            }

            HttpStatusCode.NotFound -> {
                PagedCharacters(
                    items = emptyList(),
                    hasNextPage = false
                )
            }

            else -> {
                val message = when (response.status.value) {
                    429 -> "Too many requests. Please try again in a few seconds."
                    else -> "Error loading characters: ${response.status.value}"
                }
                throw Exception(message)
            }
        }
    }

    override suspend fun getCharacterById(id: Int): Character {
        return api.getCharacterById(id).toDomain()
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return dao.observeAll().map { list -> list.map { it.toDomain() } }
    }

    override fun observeFavoriteIds(): Flow<Set<Int>> {
        return dao.observeFavoriteIds().map { it.toSet() }
    }

    override suspend fun addFavorite(character: Character) {
        dao.insert(character.toEntity())
    }

    override suspend fun removeFavorite(characterId: Int) {
        dao.deleteById(characterId)
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return dao.isFavorite(characterId)
    }
}