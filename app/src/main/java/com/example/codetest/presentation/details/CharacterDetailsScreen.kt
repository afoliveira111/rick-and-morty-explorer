package com.example.codetest.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.load(characterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            imageVector = if (state.isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Text(
                    text = "Loading details...",
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                )
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Error loading details",
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                )
            }

            state.character != null -> {
                val character = state.character!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = character.imageUrl,
                        contentDescription = character.name,
                        modifier = Modifier.size(180.dp)
                    )

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Status: ${character.status}")
                            Text("Species: ${character.species}")
                            Text("Gender: ${character.gender}")
                            Text("Origin: ${character.originName}")
                            Text("Location: ${character.locationName}")
                            Text("Episodes: ${character.episodesCount}")
                        }
                    }
                }
            }
        }
    }
}