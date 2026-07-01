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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
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
        if (_uiState.value.selectedRange == range) return
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            selectedRange = range
        )
        buildInsights(cachedSessions, range)
    }

    private fun observeSessions() {
        observeJob?.cancel()
        observeJob = activityRepo
            .observeSessionsSince("2000-01-01")
            .onEach { sessions ->
                cachedSessions = sessions
                buildInsights(
                    allSessions = sessions,
                    range = _uiState.value.selectedRange
                )
            }
            .launchIn(viewModelScope)
    }

    private fun buildInsights(
        allSessions: List<SessionEntity>,
        range: InsightsRange
    ) {
        val today = LocalDate.now()

        val datedSessions = allSessions.mapNotNull { session ->
            parseDate(session.dateLabel)?.let { date -> session to date }
        }

        val firstDataDate = datedSessions.minOfOrNull { it.second } ?: today
        val currentWindow =
            currentWindow(range = range, today = today, firstDataDate = firstDataDate)
        val previousWindow = previousWindow(currentWindow, firstDataDate)

        val current = datedSessions.filter { (_, date) ->
            !date.isBefore(currentWindow.start) && !date.isAfter(currentWindow.endInclusive)
        }

        val previous = datedSessions.filter { (_, date) ->
            !date.isBefore(previousWindow.start) && !date.isAfter(previousWindow.endInclusive)
        }

        val totalSec = current.sumOf { it.first.elapsedSeconds }
        val prevTotalSec = previous.sumOf { it.first.elapsedSeconds }

        val completed = current.count { it.first.isCompleted }
        val prevCompleted = previous.count { it.first.isCompleted }

        val completionRate = percent(
            numerator = completed,
            denominator = current.size
        )

        val prevCompletionRate = percent(
            numerator = prevCompleted,
            denominator = previous.size
        )

        val activeDays = current.map { it.second }.toSet()

        val totalDaysInRange =
            (ChronoUnit.DAYS.between(currentWindow.start, currentWindow.endInclusive) + 1).toInt()

        val consistencyPercent = percent(
            numerator = activeDays.size,
            denominator = totalDaysInRange
        )

        val currentStreak = calculateCurrentStreak(
            activeDays = activeDays,
            anchorDay = today,
            rangeStart = currentWindow.start
        )

        val longestStreak = calculateLongestStreak(activeDays)

        val trendPoints = buildTrendPoints(
            sessions = current,
            range = range,
            currentWindow = currentWindow
        )

        val highlightedTrendIndex = trendPoints
            .takeIf { points -> points.any { it.valueMinutes > 0 } }
            ?.indices
            ?.maxByOrNull { index -> trendPoints[index].valueMinutes }
            ?: -1

        val topActivities = current
            .groupBy { it.first.activityId }
            .entries
            .sortedByDescending { entry ->
                entry.value.sumOf { it.first.elapsedSeconds }
            }
            .take(3)
            .mapIndexed { index, entry ->
                val sessions = entry.value.map { it.first }
                val latest = sessions.maxByOrNull { it.id } ?: sessions.first()
                val activitySeconds = sessions.sumOf { it.elapsedSeconds }
                val sharePercent = percentLong(
                    numerator = activitySeconds,
                    denominator = totalSec
                )

                TopActivityUiModel(
                    rank = index + 1,
                    name = latest.activityName,
                    totalFocusTime = formatSeconds(activitySeconds),
                    sharePercent = sharePercent,
                    icon = latest.activityIcon
                )
            }


        val pattern = when (range) {
            InsightsRange.WEEK -> {
                val bestDayEntry = current
                    .groupBy { it.second }
                    .mapValues { entry -> entry.value.sumOf { it.first.elapsedSeconds } }
                    .filterValues { it > 0L }
                    .maxByOrNull { it.value }

                PatternInsightUiModel(
                    title = "Most Productive Day",
                    highlight = bestDayEntry?.key?.format(
                        DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
                    ) ?: "—",
                    value = bestDayEntry?.value?.let(::formatSeconds) ?: "0m",
                    description = if (bestDayEntry != null) {
                        "${bestDayEntry.key.format(DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH))} had your highest focus time this week."
                    } else {
                        "Not enough data yet."
                    },
                    miniBars = emptyList(),
                    highlightedBarIndex = -1
                )
            }

            InsightsRange.MONTH -> {
                val bestDayEntry = current
                    .groupBy { it.second }
                    .mapValues { entry -> entry.value.sumOf { it.first.elapsedSeconds } }
                    .filterValues { it > 0L }
                    .maxByOrNull { it.value }

                PatternInsightUiModel(
                    title = "Most Productive Day",
                    highlight = bestDayEntry?.key?.format(
                        DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)
                    ) ?: "—",
                    value = bestDayEntry?.value?.let(::formatSeconds) ?: "0m",
                    description = if (bestDayEntry != null) {
                        "${bestDayEntry.key.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH))} had your highest focus time this month."
                    } else {
                        "Not enough data yet."
                    },
                    miniBars = emptyList(),
                    highlightedBarIndex = -1
                )
            }

            InsightsRange.YEAR -> {
                val bestMonthEntry = current
                    .groupBy { YearMonth.from(it.second) }
                    .mapValues { entry -> entry.value.sumOf { it.first.elapsedSeconds } }
                    .filterValues { it > 0L }
                    .maxByOrNull { it.value }

                PatternInsightUiModel(
                    title = "Most Productive Month",
                    highlight = bestMonthEntry?.key?.format(
                        DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
                    ) ?: "—",
                    value = bestMonthEntry?.value?.let(::formatSeconds) ?: "0m",
                    description = if (bestMonthEntry != null) {
                        "${bestMonthEntry.key.format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH))} had your highest focus time this year."
                    } else {
                        "Not enough data yet."
                    },
                    miniBars = emptyList(),
                    highlightedBarIndex = -1
                )
            }
        }

        _uiState.value = InsightsUiState(
            isLoading = false,
            selectedRange = range,
            summary = InsightsSummary(
                totalFocusTime = formatSeconds(totalSec),
                totalFocusDelta = formatPercentDelta(
                    current = totalSec,
                    previous = prevTotalSec
                ),
                sessionsCompleted = completed,
                sessionsDelta = formatPercentDelta(
                    current = completed.toLong(),
                    previous = prevCompleted.toLong()
                ),
                completionRate = completionRate,
                completionDelta = formatPercentagePointDelta(
                    current = completionRate,
                    previous = prevCompletionRate
                )
            ),
            trend = TrendChartUiModel(
                title = "Focus Time",
                totalLabel = formatSeconds(totalSec),
                subtitle = when (range) {
                    InsightsRange.WEEK -> "Monday to Sunday"
                    InsightsRange.MONTH -> "${currentWindow.start.dayOfMonth} to ${currentWindow.endInclusive.dayOfMonth} ${
                        currentWindow.endInclusive.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.ENGLISH
                        )
                    }"

                    InsightsRange.YEAR -> "${
                        currentWindow.start.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.ENGLISH
                        )
                    } ${currentWindow.start.year} to ${
                        currentWindow.endInclusive.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.ENGLISH
                        )
                    } ${currentWindow.endInclusive.year}"
                },
                points = trendPoints,
                highlightedIndex = highlightedTrendIndex
            ),
            consistency = ConsistencyUiModel(
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                consistencyPercent = consistencyPercent,
                completedDaysText = "${activeDays.size} of $totalDaysInRange days"
            ),
            topActivities = topActivities,
            pattern = pattern
        )
    }

    private fun buildTrendPoints(
        sessions: List<Pair<SessionEntity, LocalDate>>,
        range: InsightsRange,
        currentWindow: DateWindow
    ): List<TrendPointUiModel> {
        return when (range) {
            InsightsRange.WEEK -> {
                generateSequence(currentWindow.start) { date ->
                    date.plusDays(1).takeIf { !it.isAfter(currentWindow.endInclusive) }
                }.map { day ->
                    TrendPointUiModel(
                        label = day.format(DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)),
                        valueMinutes = sessions
                            .filter { it.second == day }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }.toList()
            }

            InsightsRange.MONTH -> {
                generateSequence(currentWindow.start) { date ->
                    date.plusDays(1).takeIf { !it.isAfter(currentWindow.endInclusive) }
                }.map { day ->
                    TrendPointUiModel(
                        label = day.dayOfMonth.toString(),
                        valueMinutes = sessions
                            .filter { it.second == day }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }.toList()
            }

            InsightsRange.YEAR -> {
                val startMonth = YearMonth.from(currentWindow.start)
                val endMonth = YearMonth.from(currentWindow.endInclusive)

                generateSequence(startMonth) { month ->
                    month.plusMonths(1).takeIf { !it.isAfter(endMonth) }
                }.map { month ->
                    TrendPointUiModel(
                        label = month.format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)),
                        valueMinutes = sessions
                            .filter { (_, date) ->
                                date.year == month.year && date.month == month.month
                            }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }.toList()
            }
        }
    }

    private fun currentWindow(
        range: InsightsRange,
        today: LocalDate,
        firstDataDate: LocalDate
    ): DateWindow {
        return when (range) {
            InsightsRange.WEEK -> {
                val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val weekEnd = weekStart.plusDays(6)
                DateWindow(
                    start = maxOf(weekStart, firstDataDate),
                    endInclusive = minOf(weekEnd, today)
                )
            }

            InsightsRange.MONTH -> {
                val monthStart = today.withDayOfMonth(1)
                DateWindow(
                    start = maxOf(monthStart, firstDataDate),
                    endInclusive = today
                )
            }

            InsightsRange.YEAR -> {
                val startOfYear = today.withDayOfYear(1)
                DateWindow(
                    start = maxOf(startOfYear, firstDataDate),
                    endInclusive = today
                )
            }
        }
    }

    private fun previousWindow(
        current: DateWindow,
        firstDataDate: LocalDate
    ): DateWindow {
        val length = ChronoUnit.DAYS.between(current.start, current.endInclusive) + 1
        val previousEnd = current.start.minusDays(1)
        val previousStart = previousEnd.minusDays(length - 1)

        return if (previousEnd.isBefore(firstDataDate)) {
            DateWindow(
                start = firstDataDate,
                endInclusive = firstDataDate.minusDays(1)
            )
        } else {
            DateWindow(
                start = maxOf(previousStart, firstDataDate),
                endInclusive = previousEnd
            )
        }
    }

    private fun calculateCurrentStreak(
        activeDays: Set<LocalDate>,
        anchorDay: LocalDate,
        rangeStart: LocalDate
    ): Int {
        var streak = 0
        var checking = anchorDay

        while (!checking.isBefore(rangeStart) && activeDays.contains(checking)) {
            streak++
            checking = checking.minusDays(1)
        }

        return streak
    }

    private fun calculateLongestStreak(days: Set<LocalDate>): Int {
        if (days.isEmpty()) return 0

        val sortedDays = days.toSortedSet()
        var longest = 0
        var streak = 0
        var previousDay: LocalDate? = null

        for (day in sortedDays) {
            streak = if (previousDay?.plusDays(1) == day) {
                streak + 1
            } else {
                1
            }

            longest = maxOf(longest, streak)
            previousDay = day
        }

        return longest
    }

    private fun parseDate(value: String): LocalDate? {
        return runCatching { LocalDate.parse(value) }.getOrNull()
    }

    private fun percent(numerator: Int, denominator: Int): Int {
        if (denominator <= 0) return 0
        return ((numerator * 100f) / denominator).toInt()
    }

    private fun percentLong(numerator: Long, denominator: Long): Int {
        if (denominator <= 0L) return 0
        return ((numerator * 100f) / denominator).toInt()
    }

    private fun formatPercentDelta(current: Long, previous: Long): String {
        if (previous <= 0L) {
            return if (current > 0L) "+100%" else "0%"
        }

        val delta = (((current - previous) * 100f) / previous).toInt()
        return if (delta > 0) "+$delta%" else "$delta%"
    }

    private fun formatPercentagePointDelta(current: Int, previous: Int): String {
        val delta = current - previous
        return if (delta > 0) "+${delta}pp" else "${delta}pp"
    }

    private fun formatSeconds(seconds: Long): String {
        val hours = seconds / 3600L
        val minutes = (seconds % 3600L) / 60L
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }

    private fun Long.toMinutesInt(): Int {
        return (this / 60L).toInt()
    }

    private data class DateWindow(
        val start: LocalDate,
        val endInclusive: LocalDate
    )

}