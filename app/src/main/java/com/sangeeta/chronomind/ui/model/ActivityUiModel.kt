package com.sangeeta.chronomind.ui.model

import kotlin.collections.isNotEmpty


data class ActivityUiModel(
    val id:               Int,
    val name:             String,
    val icon:             String,
    val colorHex:         String,
    val elapsedSeconds:   Long,
    val targetSeconds:    Long,
    val isRunning:        Boolean,
    val streakDays:       Int,
    val lastActiveDate:   String,
    val continueOnMiss:   Boolean
) {
    val progress: Float
        get() = if (targetSeconds <= 0L) 0f
        else (elapsedSeconds.toFloat() / targetSeconds).coerceIn(0f, 1f)

    val elapsedFormatted: String get() {
        val h = elapsedSeconds / 3600
        val m = (elapsedSeconds % 3600) / 60
        val s = elapsedSeconds % 60
        return "%02d:%02d:%02d".format(h, m, s)
    }

    val statusLabel: String
        get() = when {
            isRunning -> "Running"
            elapsedSeconds > 0L -> "Paused"
            else -> "Ready"
        }
}






data class AllActivitiesUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val selectedSort: ActivitySortOption = ActivitySortOption.RECENTLY_USED,
    val activities: List<ActivityUiModel> = emptyList(),
    val filteredActivities: List<ActivityUiModel> = emptyList()
) {
    val isEmpty: Boolean = !isLoading && activities.isEmpty()
    val isSearchEmpty: Boolean = !isLoading && activities.isNotEmpty() && filteredActivities.isEmpty()
}

enum class ActivitySortOption(val label: String) {
    RECENTLY_USED("Recently used"),
    RECENTLY_ADDED("Recently added"),
    A_TO_Z("A–Z")
}