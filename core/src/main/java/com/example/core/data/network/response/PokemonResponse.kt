package com.example.core.data.network.response

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)
