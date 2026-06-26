package com.sangeeta.chronomind.ui.history

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private var cachedSessions: List<SessionEntity> = emptyList()

    init {
        observeSessions()
    }

    fun onRangeSelected(range: HistoryRange) {
        _uiState.update { it.copy(selectedRange = range) }
        rebuildFromCurrentSessions()
    }

    fun onFilterSelected(filter: HistoryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        rebuildFromCurrentSessions()
    }

    fun onSortSelected(sort: HistorySort) {
        _uiState.update { it.copy(selectedSort = sort) }
        rebuildFromCurrentSessions()
    }

    private fun observeSessions() {
        activityRepo.observeSessionsSince("2000-01-01")
            .onEach { sessions ->
                cachedSessions = sessions
                rebuildFromCurrentSessions()
            }
            .launchIn(viewModelScope)
    }

    private fun rebuildFromCurrentSessions() {
        val range = _uiState.value.selectedRange
        val filter = _uiState.value.selectedFilter
        val sort = _uiState.value.selectedSort
        val today = LocalDate.now()

        val cutoff = when (range) {
            HistoryRange.DAY -> today
            HistoryRange.WEEK -> today.minusDays(6)
            HistoryRange.MONTH -> today.minusDays(29)
        }

        val filtered = cachedSessions
            .filter { session ->
                val sessionDate = parseDate(session.dateLabel) ?: return@filter false
                !sessionDate.isBefore(cutoff)
            }
            .filter {
                when (filter) {
                    HistoryFilter.ALL -> true
                    HistoryFilter.COMPLETED -> it.isCompleted
                    HistoryFilter.INCOMPLETE -> !it.isCompleted
                }
            }
            .let { sessions ->
                when (sort) {
                    HistorySort.NEWEST_FIRST -> sessions.sortedByDescending { parseDate(it.dateLabel) }
                    HistorySort.OLDEST_FIRST -> sessions.sortedBy { parseDate(it.dateLabel) }
                }
            }

        val grouped = filtered
            .groupBy { it.dateLabel }
            .toSortedMap(compareByDescending { it })
            .map { (dateStr, sessions) ->
                val date = parseDate(dateStr) ?: today
                val title = when (date) {
                    today -> "Today"
                    today.minusDays(1) -> "Yesterday"
                    else -> date.format(DateTimeFormatter.ofPattern("MMM d"))
                }

                HistoryDateGroup(
                    title = title,
                    subtitleDate = date.format(DateTimeFormatter.ofPattern("yyyy")),
                    sessions = sessions.map { it.toUiModel() }
                )
            }
            .let { groups ->
                when (sort) {
                    HistorySort.NEWEST_FIRST -> groups
                    HistorySort.OLDEST_FIRST -> groups.reversed()
                }
            }

        val allSessions = grouped.flatMap { it.sessions }
        val completedCount = allSessions.count { it.isCompleted }
        val totalSeconds = filtered.sumOf { it.elapsedSeconds }

        _uiState.update {
            it.copy(
                isLoading = false,
                groupedSessions = grouped,
                summary = HistorySummary(
                    totalSessions = allSessions.size,
                    totalFocusTime = formatSeconds(totalSeconds),
                    completionRate = if (allSessions.isEmpty()) 0 else ((completedCount * 100f) / allSessions.size).toInt(),
                    rangeText = when (range) {
                        HistoryRange.DAY -> today.format(DateTimeFormatter.ofPattern("MMM d"))
                        HistoryRange.WEEK -> "${cutoff.format(DateTimeFormatter.ofPattern("MMM d"))} - ${today.format(DateTimeFormatter.ofPattern("MMM d"))}"
                        HistoryRange.MONTH -> today.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                    }
                )
            )
        }
    }

    private fun SessionEntity.toUiModel(): HistorySessionUiModel {
        val safeColor = runCatching { Color(android.graphics.Color.parseColor(activityColorHex)) }
            .getOrElse { Color(0xFFF6C445) }

        return HistorySessionUiModel(
            id = id,
            activityName = activityName,
            durationText = formatSeconds(elapsedSeconds),
            isCompleted = isCompleted,
            icon= activityIcon,
            accentColor = safeColor
        )
    }

    private fun parseDate(value: String): LocalDate? {
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }

    private fun formatSeconds(seconds: Long): String {
        val hours = seconds / 3600L
        val minutes = (seconds % 3600L) / 60L
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}