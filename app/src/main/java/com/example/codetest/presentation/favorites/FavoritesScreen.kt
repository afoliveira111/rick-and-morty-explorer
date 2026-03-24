package com.example.codetest.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codetest.presentation.components.CharacterCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val filteredItems = state.items
        .filter { character ->
            state.query.isBlank() || character.name.contains(state.query, ignoreCase = true)
        }
        .filter { character ->
            state.selectedStatus == null || character.status.equals(state.selectedStatus, ignoreCase = true)
        }
        .filter { character ->
            state.selectedGender == null || character.gender.equals(state.selectedGender, ignoreCase = true)
        }
        .let { characters ->
            when (state.sortOption) {
                FavoriteSortOption.NAME_ASC -> characters.sortedBy { it.name }
                FavoriteSortOption.NAME_DESC -> characters.sortedByDescending { it.name }
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.items.isNotEmpty()) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChange,
                    label = { Text("Search favorite") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedStatus == "alive",
                        onClick = { viewModel.onStatusSelected("alive") },
                        label = { Text("Alive") }
                    )

                    FilterChip(
                        selected = state.selectedGender == "male",
                        onClick = { viewModel.onGenderSelected("male") },
                        label = { Text("Male") }
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onSortSelected(FavoriteSortOption.NAME_ASC)
                        }
                    ) {
                        Text("Name A-Z")
                    }

                    TextButton(
                        onClick = {
                            viewModel.onSortSelected(FavoriteSortOption.NAME_DESC)
                        }
                    ) {
                        Text("Name Z-A")
                    }
                }
            }

            when {
                state.items.isEmpty() -> {
                    Text(
                        text = "No favorites yet",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                filteredItems.isEmpty() -> {
                    Text(
                        text = "No favorite characters found",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredItems) { item ->
                            CharacterCard(
                                character = item,
                                onClick = { onCharacterClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}