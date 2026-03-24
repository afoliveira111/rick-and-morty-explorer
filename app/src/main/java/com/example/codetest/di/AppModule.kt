package com.example.codetest.di

import androidx.room.Room
import com.example.codetest.data.local.AppDatabase
import com.example.codetest.data.remote.CharacterApiService
import com.example.codetest.data.repository.CharacterRepositoryImpl
import com.example.codetest.domain.repository.CharacterRepository
import com.example.codetest.domain.usecase.AddFavoriteUseCase
import com.example.codetest.domain.usecase.GetCharacterDetailsUseCase
import com.example.codetest.domain.usecase.GetCharactersUseCase
import com.example.codetest.domain.usecase.GetFavoritesUseCase
import com.example.codetest.domain.usecase.IsFavoriteUseCase
import com.example.codetest.domain.usecase.RemoveFavoriteUseCase
import com.example.codetest.presentation.details.CharacterDetailsViewModel
import com.example.codetest.presentation.favorites.FavoritesViewModel
import com.example.codetest.presentation.list.CharacterListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }

            defaultRequest {
                url("https://rickandmortyapi.com/api/")
                contentType(ContentType.Application.Json)
            }
        }
    }

    single {
        CharacterApiService(get())
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "rick_morty_db"
        ).build()
    }

    single {
        get<AppDatabase>().favoriteCharacterDao()
    }

    single<CharacterRepository> {
        CharacterRepositoryImpl(
            api = get(),
            dao = get()
        )
    }

    factory { GetCharactersUseCase(get()) }
    factory { GetCharacterDetailsUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { AddFavoriteUseCase(get()) }
    factory { RemoveFavoriteUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }

    viewModel {
        CharacterListViewModel(
            getCharactersUseCase = get()
        )
    }

    viewModel {
        CharacterDetailsViewModel(
            getCharacterDetailsUseCase = get(),
            addFavoriteUseCase = get(),
            removeFavoriteUseCase = get(),
            isFavoriteUseCase = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            getFavoritesUseCase = get()
        )
    }
}