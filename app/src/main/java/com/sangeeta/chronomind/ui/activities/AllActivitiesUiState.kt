package com.sangeeta.chronomind.ui.activities

import com.sangeeta.chronomind.ui.model.ActivityUiModel

enum class ActivitySortOption(val label: String) {
    RECENTLY_USED("Recently Used"),
    RECENTLY_ADDED("Recently Added"),
    A_TO_Z("A → Z")
}

data class AllActivitiesUiState(
    val isLoading: Boolean = true,
    val activities: List<ActivityUiModel> = emptyList(),
    val filteredActivities: List<ActivityUiModel> = emptyList(),
    val searchQuery: String = "",
    val selectedSort: ActivitySortOption = ActivitySortOption.RECENTLY_USED,
    val isEmpty: Boolean = false,
    val isSearchEmpty: Boolean = false
)
