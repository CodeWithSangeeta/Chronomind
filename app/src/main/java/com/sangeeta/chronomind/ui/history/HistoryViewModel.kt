package com.sangeeta.chronomind.ui.history


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {

    private val allGroups = mockHistoryGroups()

    private val _uiState = MutableStateFlow(
        HistoryUiState(
            isLoading = false,
            selectedRange = HistoryRange.WEEK,
            selectedFilter = HistoryFilter.ALL,
            selectedSort = HistorySort.NEWEST_FIRST
        )
    )
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        rebuildState()
    }

    fun onRangeSelected(range: HistoryRange) {
        _uiState.update { it.copy(selectedRange = range) }
        rebuildState()
    }

    fun onFilterSelected(filter: HistoryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        rebuildState()
    }

    fun onSortSelected(sort: HistorySort) {
        _uiState.update { it.copy(selectedSort = sort) }
        rebuildState()
    }

    private fun rebuildState() {
        val current = _uiState.value
        val baseGroups = groupsForRange(current.selectedRange)

        val filteredGroups = baseGroups.mapNotNull { group ->
            val sessions = group.sessions.filter { session ->
                when (current.selectedFilter) {
                    HistoryFilter.ALL -> true
                    HistoryFilter.COMPLETED -> session.isCompleted
                    HistoryFilter.INCOMPLETE -> !session.isCompleted
                }
            }

            if (sessions.isEmpty()) null else group.copy(sessions = sessions)
        }

        val sortedGroups = when (current.selectedSort) {
            HistorySort.NEWEST_FIRST -> filteredGroups
            HistorySort.OLDEST_FIRST -> filteredGroups.reversed()
        }

        val allSessions = sortedGroups.flatMap { it.sessions }
        val completedCount = allSessions.count { it.isCompleted }
        val completionRate =
            if (allSessions.isEmpty()) 0 else ((completedCount * 100f) / allSessions.size).toInt()

        val totalMinutes = allSessions.sumOf { parseMinutes(it.durationText) }

        _uiState.update {
            it.copy(
                summary = HistorySummary(
                    totalSessions = allSessions.size,
                    totalFocusTime = formatMinutes(totalMinutes),
                    completionRate = completionRate,
                    rangeText = when (current.selectedRange) {
                        HistoryRange.DAY -> "Jun 22"
                        HistoryRange.WEEK -> "Jun 16 – Jun 22"
                        HistoryRange.MONTH -> "June 2025"
                    }
                ),
                groupedSessions = sortedGroups
            )
        }
    }

    private fun groupsForRange(range: HistoryRange): List<HistoryDateGroup> {
        return when (range) {
            HistoryRange.DAY -> allGroups.take(1)
            HistoryRange.WEEK -> allGroups
            HistoryRange.MONTH -> allGroups + monthExtraGroups()
        }
    }

    private fun parseMinutes(text: String): Int {
        val cleaned = text.lowercase()
        val hourPart = Regex("(\\d+)h").find(cleaned)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val minutePart = Regex("(\\d+)m").find(cleaned)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        return (hourPart * 60) + minutePart
    }

    private fun formatMinutes(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }

    private fun mockHistoryGroups(): List<HistoryDateGroup> {
        return listOf(
            HistoryDateGroup(
                title = "Today",
                subtitleDate = "Jun 22",
                sessions = listOf(
                    HistorySessionUiModel(
                        id = 1,
                        activityName = "Creative Projects",
                        durationText = "1h 30m",
                        isCompleted = true,
                        iconEmoji = "🎨",
                        accentColor = Color(0xFFF6C445)
                    ),
                    HistorySessionUiModel(
                        id = 2,
                        activityName = "Study & Learning",
                        durationText = "2h 15m",
                        isCompleted = true,
                        iconEmoji = "📘",
                        accentColor = Color(0xFF2F7DE1)
                    ),
                    HistorySessionUiModel(
                        id = 3,
                        activityName = "Workout",
                        durationText = "45m",
                        isCompleted = false,
                        iconEmoji = "🏋️",
                        accentColor = Color(0xFF8D49D7)
                    )
                )
            ),
            HistoryDateGroup(
                title = "Yesterday",
                subtitleDate = "Jun 21",
                sessions = listOf(
                    HistorySessionUiModel(
                        id = 4,
                        activityName = "Writing & Journaling",
                        durationText = "1h 00m",
                        isCompleted = true,
                        iconEmoji = "✍️",
                        accentColor = Color(0xFF24AFC8)
                    ),
                    HistorySessionUiModel(
                        id = 5,
                        activityName = "Meditation",
                        durationText = "20m",
                        isCompleted = true,
                        iconEmoji = "🧘",
                        accentColor = Color(0xFFF08A13)
                    ),
                    HistorySessionUiModel(
                        id = 6,
                        activityName = "Coding Practice",
                        durationText = "1h 45m",
                        isCompleted = false,
                        iconEmoji = "💻",
                        accentColor = Color(0xFF4AA84F)
                    ),
                    HistorySessionUiModel(
                        id = 7,
                        activityName = "Marketing Work",
                        durationText = "1h 10m",
                        isCompleted = true,
                        iconEmoji = "📈",
                        accentColor = Color(0xFFE04B3F)
                    )
                )
            ),
            HistoryDateGroup(
                title = "Jun 18",
                subtitleDate = "2025",
                sessions = listOf(
                    HistorySessionUiModel(
                        id = 8,
                        activityName = "Study & Learning",
                        durationText = "1h 30m",
                        isCompleted = true,
                        iconEmoji = "📘",
                        accentColor = Color(0xFF2F7DE1)
                    ),
                    HistorySessionUiModel(
                        id = 9,
                        activityName = "Creative Projects",
                        durationText = "50m",
                        isCompleted = false,
                        iconEmoji = "🎨",
                        accentColor = Color(0xFF8D49D7)
                    )
                )
            ),
            HistoryDateGroup(
                title = "Jun 17",
                subtitleDate = "2025",
                sessions = listOf(
                    HistorySessionUiModel(
                        id = 10,
                        activityName = "Workout",
                        durationText = "1h 00m",
                        isCompleted = false,
                        iconEmoji = "🏋️",
                        accentColor = Color(0xFF4AA84F)
                    )
                )
            )
        )
    }

    private fun monthExtraGroups(): List<HistoryDateGroup> {
        return listOf(
            HistoryDateGroup(
                title = "Jun 14",
                subtitleDate = "2025",
                sessions = listOf(
                    HistorySessionUiModel(
                        id = 11,
                        activityName = "Client Work",
                        durationText = "1h 20m",
                        isCompleted = true,
                        iconEmoji = "💼",
                        accentColor = Color(0xFFD93B8A)
                    )
                )
            )
        )
    }
}