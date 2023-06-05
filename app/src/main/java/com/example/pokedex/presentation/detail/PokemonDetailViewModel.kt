package com.example.pokedex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.network.domain.SinglePokemonResponse
import com.example.pokedex.data.repository.SinglePokemonRepository
import com.example.pokedex.data.usecase.GetPokemonUseCase
import com.example.pokedex.data.usecase.GetSinglePokemonUseCase
import com.example.pokedex.data.usecase.base.ResultStatus
import com.example.pokedex.utils.NetworkResource
import com.example.pokedex.utils.extractId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getSinglePokemonUseCase: GetSinglePokemonUseCase
) : ViewModel() {


    fun singlePokemon(url: String): Flow<ResultStatus<SinglePokemonResponse>>{
        val id = url.extractId()
        return getSinglePokemonUseCase.invoke(
            GetSinglePokemonUseCase.GetSinglePokemonParams(id)
        )
    }
}