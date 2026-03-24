package com.example.codetest.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codetest.presentation.components.CharacterCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: CharacterListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                actions = {
                    TextButton(onClick = onFavoritesClick) {
                        Text("Favorites")
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
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                label = { Text("Search by name") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = viewModel::onSearch) {
                    Text("Search")
                }

                FilterChip(
                    selected = state.selectedStatus == "alive",
                    onClick = {
                        viewModel.onStatusSelected(
                            if (state.selectedStatus == "alive") null else "alive"
                        )
                    },
                    label = { Text("Alive") }
                )

                FilterChip(
                    selected = state.selectedGender == "male",
                    onClick = {
                        viewModel.onGenderSelected(
                            if (state.selectedGender == "male") null else "male"
                        )
                    },
                    label = { Text("Male") }
                )
            }

            when {
                state.isLoading && state.items.isEmpty() -> {
                    Text(
                        text = "Loading characters...",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                state.error != null && state.items.isEmpty() -> {
                    Text(
                        text = state.error ?: "Error loading characters",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                state.items.isEmpty() -> {
                    Text(
                        text = "No characters found",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.items) { index, item ->
                            CharacterCard(
                                character = item,
                                onClick = { onCharacterClick(item.id) }
                            )

                            if (
                                index == state.items.lastIndex &&
                                !state.isLoading &&
                                state.hasNextPage
                            ) {
                                LaunchedEffect(state.items.size) {
                                    viewModel.loadMore()
                                }
                            }
                        }

                        if (state.isLoading && state.items.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Loading more...",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}