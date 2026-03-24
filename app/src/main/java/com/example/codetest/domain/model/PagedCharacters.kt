package com.example.codetest.domain.model


data class PagedCharacters(
    val items: List<Character>,
    val hasNextPage: Boolean
)