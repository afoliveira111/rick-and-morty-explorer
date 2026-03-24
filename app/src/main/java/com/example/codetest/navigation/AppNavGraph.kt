package com.example.codetest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.codetest.presentation.details.CharacterDetailsScreen
import com.example.codetest.presentation.favorites.FavoritesScreen
import com.example.codetest.presentation.list.CharacterListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LIST
    ) {
        composable(AppDestinations.LIST) {
            CharacterListScreen(
                onCharacterClick = { id ->
                    navController.navigate("${AppDestinations.DETAILS}/$id")
                },
                onFavoritesClick = {
                    navController.navigate(AppDestinations.FAVORITES)
                }
            )
        }

        composable(
            route = "${AppDestinations.DETAILS}/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0

            CharacterDetailsScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.FAVORITES) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onCharacterClick = { id ->
                    navController.navigate("${AppDestinations.DETAILS}/$id")
                }
            )
        }
    }
}