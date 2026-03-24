package com.example.codetest.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class SimpleLocationDto(
    val name: String,
    val url: String
)