package com.example.codetest.data.mapper

import com.example.codetest.data.local.FavoriteCharacterEntity
import com.example.codetest.data.remote.dto.CharacterDto
import com.example.codetest.domain.model.Character

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        imageUrl = image,
        episodesCount = episode.size
    )
}

fun FavoriteCharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodesCount = episodesCount
    )
}

fun Character.toEntity(): FavoriteCharacterEntity {
    return FavoriteCharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodesCount = episodesCount
    )
}