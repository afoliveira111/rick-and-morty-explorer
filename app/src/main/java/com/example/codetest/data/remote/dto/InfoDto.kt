package com.example.codetest.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)