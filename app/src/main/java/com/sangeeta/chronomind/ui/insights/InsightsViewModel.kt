package com.sangeeta.chronomind.ui.insights


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangeeta.chronomind.local.db.entity.SessionEntity
import com.sangeeta.chronomind.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState(isLoading = true))
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null
    private var cachedSessions: List<SessionEntity> = emptyList()

    init {
        observeSessions()
    }

    fun onRangeSelected(range: InsightsRange) {
        _uiState.value = _uiState.value.copy(selectedRange = range)
        buildInsights(cachedSessions, range)
    }

    private fun observeSessions() {
        observeJob?.cancel()
        observeJob = activityRepo
            .observeSessionsSince("2000-01-01")
            .onEach { sessions ->
                cachedSessions = sessions
                buildInsights(sessions, _uiState.value.selectedRange)
            }
            .launchIn(viewModelScope)
    }

    private fun buildInsights(all: List<SessionEntity>, range: InsightsRange) {
        val today = LocalDate.now()

        val cutoff = when (range) {
            InsightsRange.WEEK -> today.minusDays(6)
            InsightsRange.MONTH -> today.minusDays(29)
            InsightsRange.YEAR -> today.minusYears(1).plusDays(1)
        }

        val current = all.filter { session ->
            parseDate(session.dateLabel)?.let { !it.isBefore(cutoff) } == true
        }

        val previousWindowDays = when (range) {
            InsightsRange.WEEK -> 7L
            InsightsRange.MONTH -> 30L
            InsightsRange.YEAR -> ChronoUnit.DAYS.between(cutoff, today) + 1
        }

        val previousStart = cutoff.minusDays(previousWindowDays)
        val previousEnd = cutoff.minusDays(1)

        val previous = all.filter { session ->
            val date = parseDate(session.dateLabel) ?: return@filter false
            !date.isBefore(previousStart) && !date.isAfter(previousEnd)
        }

        val totalSec = current.sumOf { it.elapsedSeconds }
        val prevTotalSec = previous.sumOf { it.elapsedSeconds }

        val completed = current.count { it.isCompleted }
        val prevCompleted = previous.count { it.isCompleted }

        val completionRate =
            if (current.isEmpty()) 0 else ((completed * 100f) / current.size).toInt()

        val prevRate =
            if (previous.isEmpty()) 0 else ((prevCompleted * 100f) / previous.size).toInt()

        val trendPoints = buildTrendPoints(current, range, today)

        val activeDays = current.mapNotNull { parseDate(it.dateLabel)?.toString() }.toSet()
        val totalDays = (ChronoUnit.DAYS.between(cutoff, today) + 1).toInt()
        val consistencyPercent =
            if (totalDays <= 0) 0 else ((activeDays.size * 100f) / totalDays).toInt()

        var currentStreak = 0
        var checkingDay = today
        while (activeDays.contains(checkingDay.toString())) {
            currentStreak++
            checkingDay = checkingDay.minusDays(1)
        }

        val sortedDays = all
            .mapNotNull { parseDate(it.dateLabel) }
            .toSortedSet()

        var longestStreak = 0
        var tempStreak = 0
        var previousDay: LocalDate? = null

        for (day in sortedDays) {
            tempStreak = if (previousDay != null && previousDay.plusDays(1) == day) {
                tempStreak + 1
            } else {
                1
            }
            if (tempStreak > longestStreak) longestStreak = tempStreak
            previousDay = day
        }

        val topActivities = current
            .groupBy { it.activityName to it.activityIcon }
            .entries
            .sortedByDescending { entry -> entry.value.sumOf { it.elapsedSeconds } }
            .take(3)
            .mapIndexed { index, entry ->
                val activitySeconds = entry.value.sumOf { it.elapsedSeconds }
                val sharePercent =
                    if (totalSec == 0L) 0 else ((activitySeconds * 100f) / totalSec).toInt()

                TopActivityUiModel(
                    rank = index + 1,
                    name = entry.key.first,
                    totalFocusTime = formatSeconds(activitySeconds),
                    sharePercent = sharePercent,
                    icon = entry.key.second
                )
            }

        val weekdayOrder = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dayMap = current
            .groupBy { session ->
                parseDate(session.dateLabel)
                    ?.dayOfWeek
                    ?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    ?: ""
            }
            .mapValues { entry -> entry.value.sumOf { it.elapsedSeconds } }

        val orderedDayValues = weekdayOrder.map { day ->
            ((dayMap[day] ?: 0L) / 60L).toInt()
        }

        val bestDayEntry = dayMap.maxByOrNull { it.value }
        val highlightedBarIndex =
            weekdayOrder.indexOf(bestDayEntry?.key).takeIf { it >= 0 } ?: -1

        _uiState.value = InsightsUiState(
            isLoading = false,
            selectedRange = range,
            summary = InsightsSummary(
                totalFocusTime = formatSeconds(totalSec),
                totalFocusDelta = formatDelta(deltaPercent(totalSec, prevTotalSec)),
                sessionsCompleted = completed,
                sessionsDelta = formatDelta(deltaPercent(completed.toLong(), prevCompleted.toLong())),
                completionRate = completionRate,
                completionDelta = formatDelta((completionRate - prevRate).toLong())
            ),
            trend = TrendChartUiModel(
                title = "Focus Time Trend",
                totalLabel = formatSeconds(totalSec),
                subtitle = when (range) {
                    InsightsRange.WEEK -> "Total this week"
                    InsightsRange.MONTH -> "Total this month"
                    InsightsRange.YEAR -> "Total this year"
                },
                points = trendPoints,
                highlightedIndex = trendPoints.indices.maxByOrNull { index ->
                    trendPoints[index].valueMinutes
                } ?: -1
            ),
            consistency = ConsistencyUiModel(
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                consistencyPercent = consistencyPercent,
                completedDaysText = "${activeDays.size} of $totalDays days"
            ),
            topActivities = topActivities,
            pattern = PatternInsightUiModel(
                title = "Most Productive Day",
                highlight = bestDayEntry?.key ?: "—",
                value = formatSeconds(bestDayEntry?.value ?: 0L),
                description = if (bestDayEntry != null) {
                    "${bestDayEntry.key} is your strongest focus day."
                } else {
                    "Not enough data yet."
                },
                miniBars = orderedDayValues,
                highlightedBarIndex = highlightedBarIndex
            )
        )
    }

    private fun buildTrendPoints(
        sessions: List<SessionEntity>,
        range: InsightsRange,
        today: LocalDate
    ): List<TrendPointUiModel> {
        return when (range) {
            InsightsRange.WEEK -> {
                (6 downTo 0).map { daysAgo ->
                    val day = today.minusDays(daysAgo.toLong())
                    val label = day.format(DateTimeFormatter.ofPattern("EEE"))
                    val minutes = sessions
                        .filter { parseDate(it.dateLabel) == day }
                        .sumOf { (it.elapsedSeconds / 60L).toInt() }

                    TrendPointUiModel(
                        label = label,
                        valueMinutes = minutes
                    )
                }
            }

            InsightsRange.MONTH -> {
                (3 downTo 0).map { weeksAgo ->
                    val weekEnd = today.minusDays((weeksAgo * 7L))
                    val weekStart = weekEnd.minusDays(6)
                    val label = "W${4 - weeksAgo}"

                    val minutes = sessions
                        .filter { session ->
                            val date = parseDate(session.dateLabel) ?: return@filter false
                            !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                        }
                        .sumOf { (it.elapsedSeconds / 60L).toInt() }

                    TrendPointUiModel(
                        label = label,
                        valueMinutes = minutes
                    )
                }
            }

            InsightsRange.YEAR -> {
                (11 downTo 0).map { monthsAgo ->
                    val monthDate = today.minusMonths(monthsAgo.toLong())
                    val label = monthDate.format(DateTimeFormatter.ofPattern("MMM"))

                    val minutes = sessions
                        .filter { session ->
                            val date = parseDate(session.dateLabel) ?: return@filter false
                            date.month == monthDate.month && date.year == monthDate.year
                        }
                        .sumOf { (it.elapsedSeconds / 60L).toInt() }

                    TrendPointUiModel(
                        label = label,
                        valueMinutes = minutes
                    )
                }
            }
        }
    }

    private fun parseDate(value: String): LocalDate? {
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }

    private fun deltaPercent(current: Long, previous: Long): Long {
        if (previous <= 0L) return 0L
        return (((current - previous) * 100f) / previous).toLong()
    }

    private fun formatDelta(value: Long): String {
        return if (value > 0) "+$value%" else "$value%"
    }

    private fun formatSeconds(seconds: Long): String {
        val hours = seconds / 3600L
        val minutes = (seconds % 3600L) / 60L
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}