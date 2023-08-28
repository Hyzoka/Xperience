package com.hyzoka.xperience.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyzoka.xperience.repository.PositionRepository
import com.hyzoka.xperience.utils.Resource
import com.hyzoka.xperience.utils.networkState.PositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject
constructor(
    private var positionRepository: PositionRepository,
) : ViewModel() {

    private val _positions = MutableStateFlow(PositionState())
    val positions: StateFlow<PositionState> = _positions

    fun getPositionsData() {
        positionRepository.getPositionsData().onEach {
            when (it) {
                is Resource.Loading -> {
                    _positions.value = PositionState(isLoading = true)
                }
                is Resource.Error -> {
                    _positions.value = PositionState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _positions.value = PositionState(data = it.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}
