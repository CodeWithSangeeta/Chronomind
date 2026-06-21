package com.sangeeta.chronomind.ui.history


import androidx.compose.ui.graphics.Color

data class HistoryUiState(
    val isLoading: Boolean = true,
    val selectedRange: HistoryRange = HistoryRange.WEEK,
    val selectedFilter: HistoryFilter = HistoryFilter.ALL,
    val selectedSort: HistorySort = HistorySort.NEWEST_FIRST,
    val summary: HistorySummary = HistorySummary(),
    val groupedSessions: List<HistoryDateGroup> = emptyList()
) {
    val isEmpty: Boolean
        get() = !isLoading && groupedSessions.isEmpty()
}

data class HistorySummary(
    val totalSessions: Int = 0,
    val totalFocusTime: String = "0m",
    val completionRate: Int = 0,
    val rangeText: String = ""
)

data class HistoryDateGroup(
    val title: String,
    val subtitleDate: String,
    val sessions: List<HistorySessionUiModel>
)

data class HistorySessionUiModel(
    val id: Int,
    val activityName: String,
    val durationText: String,
    val isCompleted: Boolean,
    val iconEmoji: String,
    val accentColor: Color
) {
    val statusLabel: String
        get() = if (isCompleted) "Completed" else "Incomplete"
}

enum class HistoryRange(val label: String) {
    DAY("Today"),
    WEEK("This Week"),
    MONTH("This Month")
}

enum class HistoryFilter(val label: String) {
    ALL("All"),
    COMPLETED("Completed"),
    INCOMPLETE("Incomplete")
}

enum class HistorySort(val label: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First")
}