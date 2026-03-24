package com.example.codetest

import com.example.codetest.domain.model.Character
import com.example.codetest.domain.model.PagedCharacters
import com.example.codetest.domain.repository.CharacterRepository
import com.example.codetest.domain.usecase.GetCharactersUseCase
import com.example.codetest.presentation.list.CharacterListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class FakeCharacterRepositoryForViewModel : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ): PagedCharacters {
        val allItems = listOf(
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
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                gender = "Male",
                originName = "Earth",
                locationName = "Earth",
                imageUrl = "https://example.com/morty.png",
                episodesCount = 41
            )
        )

        val filtered = if (!name.isNullOrBlank()) {
            allItems.filter { it.name.contains(name, ignoreCase = true) }
        } else {
            allItems
        }

        return PagedCharacters(
            items = filtered,
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

class CharacterListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loadInitial should populate state with characters`() = runTest {
        val repository = FakeCharacterRepositoryForViewModel()
        val viewModel = CharacterListViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository)
        )

        viewModel.loadInitial()

        val state = viewModel.uiState.value

        assertEquals(2, state.items.size)
        assertEquals("Rick Sanchez", state.items.first().name)
        assertNull(state.error)
    }

    @Test
    fun `onQueryChange should update query and search should filter results`() = runTest {
        val repository = FakeCharacterRepositoryForViewModel()
        val viewModel = CharacterListViewModel(
            getCharactersUseCase = GetCharactersUseCase(repository)
        )

        viewModel.onQueryChange("Morty")
        viewModel.onSearch()

        val state = viewModel.uiState.value

        assertEquals("Morty", state.query)
        assertEquals(1, state.items.size)
        assertEquals("Morty Smith", state.items.first().name)
    }
}