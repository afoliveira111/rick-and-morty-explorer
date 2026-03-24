package com.example.codetest

import com.example.codetest.domain.model.Character
import com.example.codetest.domain.model.PagedCharacters
import com.example.codetest.domain.repository.CharacterRepository
import com.example.codetest.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class FakeCharacterRepositoryForUseCase : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ): PagedCharacters {
        return PagedCharacters(
            items = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    originName = "Earth",
                    locationName = "Citadel of Ricks",
                    imageUrl = "https://example.com/rick.png",
                    episodesCount = 51
                )
            ),
            hasNextPage = false
        )
    }

    override suspend fun getCharacterById(id: Int): Character {
        throw NotImplementedError()
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> = flowOf(emptyList())

    override fun observeFavoriteIds(): Flow<Set<Int>> = flowOf(emptySet())

    override suspend fun addFavorite(character: Character) = Unit

    override suspend fun removeFavorite(characterId: Int) = Unit

    override suspend fun isFavorite(characterId: Int): Boolean = false
}

class GetCharactersUseCaseTest {

    @Test
    fun `should return characters from repository`() = runTest {
        val repository = FakeCharacterRepositoryForUseCase()
        val useCase = GetCharactersUseCase(repository)

        val result = useCase(
            page = 1,
            name = null,
            status = null,
            gender = null
        )

        assertEquals(1, result.items.size)
        assertEquals("Rick Sanchez", result.items.first().name)
        assertFalse(result.hasNextPage)
    }
}