package com.example.pokedex.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.pokedex.data.network.domain.PokemonResult
import com.example.pokedex.databinding.FragmentHomeBinding
import com.example.pokedex.presentation.home.adapter.HomeAdapter
import com.example.pokedex.presentation.home.adapter.PokemonLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var pokemonAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPokemonAdapter()
        observeInitialLoadState()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pokemonPagingData("").collect{ pagingData ->
                    pokemonAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun initPokemonAdapter() {
        pokemonAdapter = HomeAdapter { pokemonResult: PokemonResult, view: View, dominantColor: Int, picture: String? ->
            val extras = FragmentNavigatorExtras(
                view to pokemonResult.name
            )

            val directions = HomeFragmentDirections
                .actionHomeFragmentToPokemonDetailFragment(
                    pokemonResult.name.capitalize(),
                    pokemonResult,
                    picture
                )
            findNavController().navigate(directions, extras)
        }
        with(binding.recyclerPokemon){
            setHasFixedSize(true)
            adapter = pokemonAdapter.withLoadStateFooter(
               footer = PokemonLoadStateAdapter { pokemonAdapter.retry() }
            )
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            pokemonAdapter.loadStateFlow.collectLatest { loadState ->
                binding.flipperPokemon.displayedChild = when(loadState.refresh){
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_POKEMON
                    }
                    is LoadState.Error -> {
                        setShimmerVisibility(false)
                        binding.includeViewPokemonErrorState.buttonRetry.setOnClickListener {
                            pokemonAdapter.refresh()
                        }
                        FLIPPER_CHILD_ERROR
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewPokemonLoadingState.shimmerPokemon.run{
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_POKEMON = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}