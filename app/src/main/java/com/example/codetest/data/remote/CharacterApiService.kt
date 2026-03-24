package com.example.codetest.data.remote

import com.example.codetest.data.remote.dto.CharacterDto
import com.example.codetest.data.remote.dto.CharacterResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class CharacterApiService(
    private val client: HttpClient
) {
    suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?
    ): HttpResponse {
        return client.get("character") {
            parameter("page", page)
            name?.takeIf { it.isNotBlank() }?.let { parameter("name", it) }
            status?.takeIf { it.isNotBlank() }?.let { parameter("status", it) }
            gender?.takeIf { it.isNotBlank() }?.let { parameter("gender", it) }
        }
    }

    suspend fun getCharacterResponseBody(response: HttpResponse): CharacterResponseDto {
        return response.body()
    }

    suspend fun getCharacterById(id: Int): CharacterDto {
        return client.get("character/$id").body()
    }
}