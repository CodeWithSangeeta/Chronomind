package com.sangeeta.chronomind.ui.history


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.LocalDate
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//@HiltViewModel
//class HistoryViewModel @Inject constructor() : ViewModel() {
//
//    private val allGroups = mockHistoryGroups()
//
//    private val _uiState = MutableStateFlow(
//        HistoryUiState(
//            isLoading = false,
//            selectedRange = HistoryRange.WEEK,
//            selectedFilter = HistoryFilter.ALL,
//            selectedSort = HistorySort.NEWEST_FIRST
//        )
//    )
//    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
//
//    init {
//        rebuildState()
//    }
//
//    fun onRangeSelected(range: HistoryRange) {
//        _uiState.update { it.copy(selectedRange = range) }
//        rebuildState()
//    }
//
//    fun onFilterSelected(filter: HistoryFilter) {
//        _uiState.update { it.copy(selectedFilter = filter) }
//        rebuildState()
//    }
//
//    fun onSortSelected(sort: HistorySort) {
//        _uiState.update { it.copy(selectedSort = sort) }
//        rebuildState()
//    }
//
//    private fun rebuildState() {
//        val current = _uiState.value
//        val baseGroups = groupsForRange(current.selectedRange)
//
//        val filteredGroups = baseGroups.mapNotNull { group ->
//            val sessions = group.sessions.filter { session ->
//                when (current.selectedFilter) {
//                    HistoryFilter.ALL -> true
//                    HistoryFilter.COMPLETED -> session.isCompleted
//                    HistoryFilter.INCOMPLETE -> !session.isCompleted
//                }
//            }
//
//            if (sessions.isEmpty()) null else group.copy(sessions = sessions)
//        }
//
//        val sortedGroups = when (current.selectedSort) {
//            HistorySort.NEWEST_FIRST -> filteredGroups
//            HistorySort.OLDEST_FIRST -> filteredGroups.reversed()
//        }
//
//        val allSessions = sortedGroups.flatMap { it.sessions }
//        val completedCount = allSessions.count { it.isCompleted }
//        val completionRate =
//            if (allSessions.isEmpty()) 0 else ((completedCount * 100f) / allSessions.size).toInt()
//
//        val totalMinutes = allSessions.sumOf { parseMinutes(it.durationText) }
//
//        _uiState.update {
//            it.copy(
//                summary = HistorySummary(
//                    totalSessions = allSessions.size,
//                    totalFocusTime = formatMinutes(totalMinutes),
//                    completionRate = completionRate,
//                    rangeText = when (current.selectedRange) {
//                        HistoryRange.DAY -> "Jun 22"
//                        HistoryRange.WEEK -> "Jun 16 – Jun 22"
//                        HistoryRange.MONTH -> "June 2025"
//                    }
//                ),
//                groupedSessions = sortedGroups
//            )
//        }
//    }
//
//    private fun groupsForRange(range: HistoryRange): List<HistoryDateGroup> {
//        return when (range) {
//            HistoryRange.DAY -> allGroups.take(1)
//            HistoryRange.WEEK -> allGroups
//            HistoryRange.MONTH -> allGroups + monthExtraGroups()
//        }
//    }
//
//    private fun parseMinutes(text: String): Int {
//        val cleaned = text.lowercase()
//        val hourPart = Regex("(\\d+)h").find(cleaned)?.groupValues?.get(1)?.toIntOrNull() ?: 0
//        val minutePart = Regex("(\\d+)m").find(cleaned)?.groupValues?.get(1)?.toIntOrNull() ?: 0
//        return (hourPart * 60) + minutePart
//    }
//
//    private fun formatMinutes(totalMinutes: Int): String {
//        val hours = totalMinutes / 60
//        val minutes = totalMinutes % 60
//        return when {
//            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
//            hours > 0 -> "${hours}h"
//            else -> "${minutes}m"
//        }
//    }
//
//    private fun mockHistoryGroups(): List<HistoryDateGroup> {
//        return listOf(
//            HistoryDateGroup(
//                title = "Today",
//                subtitleDate = "Jun 22",
//                sessions = listOf(
//                    HistorySessionUiModel(
//                        id = 1,
//                        activityName = "Creative Projects",
//                        durationText = "1h 30m",
//                        isCompleted = true,
//                        iconEmoji = "🎨",
//                        accentColor = Color(0xFFF6C445)
//                    ),
//                    HistorySessionUiModel(
//                        id = 2,
//                        activityName = "Study & Learning",
//                        durationText = "2h 15m",
//                        isCompleted = true,
//                        iconEmoji = "📘",
//                        accentColor = Color(0xFF2F7DE1)
//                    ),
//                    HistorySessionUiModel(
//                        id = 3,
//                        activityName = "Workout",
//                        durationText = "45m",
//                        isCompleted = false,
//                        iconEmoji = "🏋️",
//                        accentColor = Color(0xFF8D49D7)
//                    )
//                )
//            ),
//            HistoryDateGroup(
//                title = "Yesterday",
//                subtitleDate = "Jun 21",
//                sessions = listOf(
//                    HistorySessionUiModel(
//                        id = 4,
//                        activityName = "Writing & Journaling",
//                        durationText = "1h 00m",
//                        isCompleted = true,
//                        iconEmoji = "✍️",
//                        accentColor = Color(0xFF24AFC8)
//                    ),
//                    HistorySessionUiModel(
//                        id = 5,
//                        activityName = "Meditation",
//                        durationText = "20m",
//                        isCompleted = true,
//                        iconEmoji = "🧘",
//                        accentColor = Color(0xFFF08A13)
//                    ),
//                    HistorySessionUiModel(
//                        id = 6,
//                        activityName = "Coding Practice",
//                        durationText = "1h 45m",
//                        isCompleted = false,
//                        iconEmoji = "💻",
//                        accentColor = Color(0xFF4AA84F)
//                    ),
//                    HistorySessionUiModel(
//                        id = 7,
//                        activityName = "Marketing Work",
//                        durationText = "1h 10m",
//                        isCompleted = true,
//                        iconEmoji = "📈",
//                        accentColor = Color(0xFFE04B3F)
//                    )
//                )
//            ),
//            HistoryDateGroup(
//                title = "Jun 18",
//                subtitleDate = "2025",
//                sessions = listOf(
//                    HistorySessionUiModel(
//                        id = 8,
//                        activityName = "Study & Learning",
//                        durationText = "1h 30m",
//                        isCompleted = true,
//                        iconEmoji = "📘",
//                        accentColor = Color(0xFF2F7DE1)
//                    ),
//                    HistorySessionUiModel(
//                        id = 9,
//                        activityName = "Creative Projects",
//                        durationText = "50m",
//                        isCompleted = false,
//                        iconEmoji = "🎨",
//                        accentColor = Color(0xFF8D49D7)
//                    )
//                )
//            ),
//            HistoryDateGroup(
//                title = "Jun 17",
//                subtitleDate = "2025",
//                sessions = listOf(
//                    HistorySessionUiModel(
//                        id = 10,
//                        activityName = "Workout",
//                        durationText = "1h 00m",
//                        isCompleted = false,
//                        iconEmoji = "🏋️",
//                        accentColor = Color(0xFF4AA84F)
//                    )
//                )
//            )
//        )
//    }
//
//    private fun monthExtraGroups(): List<HistoryDateGroup> {
//        return listOf(
//            HistoryDateGroup(
//                title = "Jun 14",
//                subtitleDate = "2025",
//                sessions = listOf(
//                    HistorySessionUiModel(
//                        id = 11,
//                        activityName = "Client Work",
//                        durationText = "1h 20m",
//                        isCompleted = true,
//                        iconEmoji = "💼",
//                        accentColor = Color(0xFFD93B8A)
//                    )
//                )
//            )
//        )
//    }
//}






@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeSessions()
    }

    private fun observeSessions() {
        // Observe all sessions; filter/group in real-time
        activityRepo.observeSessionsSince("2000-01-01") // all-time baseline
            .onEach { sessions -> rebuildState(sessions) }
            .launchIn(viewModelScope)
    }

    fun onRangeSelected(range: HistoryRange) {
        _uiState.update { it.copy(selectedRange = range) }
        // Re-trigger with new range — re-collect from current data
        rebuildFromCurrentSessions(range)
    }

    fun onFilterSelected(filter: HistoryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        rebuildFromCurrentSessions()
    }

    fun onSortSelected(sort: HistorySort) {
        _uiState.update { it.copy(selectedSort = sort) }
        rebuildFromCurrentSessions()
    }

    private var cachedSessions: List<SessionEntity> = emptyList()

    private fun rebuildState(sessions: List<SessionEntity>) {
        cachedSessions = sessions
        rebuildFromCurrentSessions()
    }

    private fun rebuildFromCurrentSessions(
        range: HistoryRange = _uiState.value.selectedRange,
        filter: HistoryFilter = _uiState.value.selectedFilter,
        sort: HistorySort = _uiState.value.selectedSort
    ) {
        val today = LocalDate.now()

        // Date cutoff based on range
        val cutoff = when (range) {
            HistoryRange.DAY   -> today
            HistoryRange.WEEK  -> today.minusDays(6)
            HistoryRange.MONTH -> today.minusDays(29)
        }

        val filtered = cachedSessions
            .filter {
                val sessionDate = LocalDate.parse(it.dateLabel)
                !sessionDate.isBefore(cutoff)
            }
            .filter {
                when (filter) {
                    HistoryFilter.ALL       -> true
                    HistoryFilter.COMPLETED -> it.isCompleted
                    HistoryFilter.INCOMPLETE -> !it.isCompleted
                }
            }
            .let {
                if (sort == HistorySort.OLDEST_FIRST) it.reversed() else it
            }

        // Group by date label
        val grouped = filtered
            .groupBy { it.dateLabel }
            .entries
            .sortedByDescending { it.key }
            .map { (dateStr, sessions) ->
                val date = LocalDate.parse(dateStr)
                val title = when {
                    date == today            -> "Today"
                    date == today.minusDays(1) -> "Yesterday"
                    else                     -> date.format(DateTimeFormatter.ofPattern("MMM d"))
                }
                HistoryDateGroup(
                    title       = title,
                    subtitleDate = date.format(DateTimeFormatter.ofPattern("yyyy")),
                    sessions    = sessions.map { it.toUiModel() }
                )
            }

        val allSessions = grouped.flatMap { it.sessions }
        val completed = allSessions.count { it.isCompleted }
        val totalSec = cachedSessions.sumOf { it.elapsedSeconds }

        _uiState.update {
            it.copy(
                isLoading      = false,
                selectedRange  = range,
                selectedFilter = filter,
                selectedSort   = sort,
                groupedSessions = grouped,
                summary = HistorySummary(
                    totalSessions   = allSessions.size,
                    totalFocusTime  = formatSeconds(totalSec),
                    completionRate  = if (allSessions.isEmpty()) 0
                    else (completed * 100f / allSessions.size).toInt(),
                    rangeText       = when (range) {
                        HistoryRange.DAY   -> today.format(DateTimeFormatter.ofPattern("MMM d"))
                        HistoryRange.WEEK  -> "${cutoff.format(DateTimeFormatter.ofPattern("MMM d"))} – ${today.format(DateTimeFormatter.ofPattern("MMM d"))}"
                        HistoryRange.MONTH -> today.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                    }
                )
            )
        }
    }

    private fun SessionEntity.toUiModel() = HistorySessionUiModel(
        id           = id,
        activityName = activityName,
        durationText = formatSeconds(elapsedSeconds),
        isCompleted  = isCompleted,
        iconEmoji    = activityIcon,
        accentColor  = Color(android.graphics.Color.parseColor("#$activityColorHex"))
    )

    private fun formatSeconds(sec: Long): String {
        val h = sec / 3600
        val m = (sec % 3600) / 60
        return if (h > 0) "${h}h ${m}m" else "${m}m"
    }
}