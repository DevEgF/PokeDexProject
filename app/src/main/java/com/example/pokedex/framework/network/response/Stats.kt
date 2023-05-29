package com.example.pokedex.framework.network.response

@Suppress("ConstructorParameterNaming")
data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
)