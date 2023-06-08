package com.example.pokedex.framework.repository

import com.example.pokedex.data.network.domain.SinglePokemonResponse
import com.example.pokedex.data.network.datasource.PokemonRemoteDataSource
import com.example.pokedex.data.repository.SinglePokemonRepository
import javax.inject.Inject

class SinglePokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource
): SinglePokemonRepository {

    override suspend fun getSinglePokemon(id: Int): SinglePokemonResponse {
        return remoteDataSource.fetchSinglePokemon(id)
    }
}