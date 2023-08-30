package com.example.pokedex.domain.model

@Suppress("ConstructorParameterNaming")
data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
)