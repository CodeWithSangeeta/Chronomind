package com.sangeeta.chronomind.ui.home


data class HomeUiState(
    val userName: String = "",
    val greeting: String = "",
    val currentFocus: HomeFocusCardUi? = null,
    val todayProgressText: String = "0m",
    val activities: List<HomeActivityItemUi> = emptyList(),
    val isLoading: Boolean = true
)

data class HomeFocusCardUi(
    val id: Int,
    val title: String,
    val formattedElapsed: String,
    val status: String,
    val progress: Float
)

data class HomeActivityItemUi(
    val id: Int,
    val title: String,
    val formattedElapsed: String,
    val status: String,
    val progress: Float,
    val isRunning: Boolean
)