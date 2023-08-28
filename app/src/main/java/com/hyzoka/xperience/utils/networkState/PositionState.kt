package com.hyzoka.xperience.utils.networkState

import com.hyzoka.xperience.model.Positions

data class PositionState(
    val data: List<Positions?> = listOf(),
    val error: String = "",
    val isLoading: Boolean = false
)