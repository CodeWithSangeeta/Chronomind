//package com.sangeeta.chronomind.ui.insights
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.sangeeta.chronomind.local.db.entity.SessionEntity
//import com.sangeeta.chronomind.repository.ActivityRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.time.format.TextStyle
//import java.time.temporal.ChronoUnit
//import java.util.Locale
//import javax.inject.Inject
//
//@HiltViewModel
//class InsightsViewModel @Inject constructor(
//    private val activityRepo: ActivityRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(InsightsUiState(isLoading = true))
//    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()
//
//    private var observeJob: Job? = null
//    private var cachedSessions: List<SessionEntity> = emptyList()
//
//    init {
//        observeSessions()
//    }
//
//    fun onRangeSelected(range: InsightsRange) {
//        _uiState.value = _uiState.value.copy(selectedRange = range)
//        buildInsights(cachedSessions, range)
//    }
//
//    private fun observeSessions() {
//        observeJob?.cancel()
//        observeJob = activityRepo
//            .observeSessionsSince("2000-01-01")
//            .onEach { sessions ->
//                cachedSessions = sessions
//                buildInsights(sessions, _uiState.value.selectedRange)
//            }
//            .launchIn(viewModelScope)
//    }
//
//    private fun buildInsights(all: List<SessionEntity>, range: InsightsRange) {
//        val today = LocalDate.now()
//
//        val cutoff = when (range) {
//            InsightsRange.WEEK -> today.minusDays(6)
//            InsightsRange.MONTH -> today.minusDays(29)
//            InsightsRange.YEAR -> today.minusYears(1).plusDays(1)
//        }
//
//        val current = all.filter { session ->
//            parseDate(session.dateLabel)?.let { !it.isBefore(cutoff) } == true
//        }
//
//        val previousWindowDays = when (range) {
//            InsightsRange.WEEK -> 7L
//            InsightsRange.MONTH -> 30L
//            InsightsRange.YEAR -> ChronoUnit.DAYS.between(cutoff, today) + 1
//        }
//
//        val previousStart = cutoff.minusDays(previousWindowDays)
//        val previousEnd = cutoff.minusDays(1)
//
//        val previous = all.filter { session ->
//            val date = parseDate(session.dateLabel) ?: return@filter false
//            !date.isBefore(previousStart) && !date.isAfter(previousEnd)
//        }
//
//        val totalSec = current.sumOf { it.elapsedSeconds }
//        val prevTotalSec = previous.sumOf { it.elapsedSeconds }
//
//        val completed = current.count { it.isCompleted }
//        val prevCompleted = previous.count { it.isCompleted }
//
//        val completionRate =
//            if (current.isEmpty()) 0 else ((completed * 100f) / current.size).toInt()
//
//        val prevRate =
//            if (previous.isEmpty()) 0 else ((prevCompleted * 100f) / previous.size).toInt()
//
//        val trendPoints = buildTrendPoints(current, range, today)
//
//        val activeDays = current.mapNotNull { parseDate(it.dateLabel)?.toString() }.toSet()
//        val totalDays = (ChronoUnit.DAYS.between(cutoff, today) + 1).toInt()
//        val consistencyPercent =
//            if (totalDays <= 0) 0 else ((activeDays.size * 100f) / totalDays).toInt()
//
//        var currentStreak = 0
//        var checkingDay = today
//        while (activeDays.contains(checkingDay.toString())) {
//            currentStreak++
//            checkingDay = checkingDay.minusDays(1)
//        }
//
//        val sortedDays = all
//            .mapNotNull { parseDate(it.dateLabel) }
//            .toSortedSet()
//
//        var longestStreak = 0
//        var tempStreak = 0
//        var previousDay: LocalDate? = null
//
//        for (day in sortedDays) {
//            tempStreak = if (previousDay != null && previousDay.plusDays(1) == day) {
//                tempStreak + 1
//            } else {
//                1
//            }
//            if (tempStreak > longestStreak) longestStreak = tempStreak
//            previousDay = day
//        }
//
//        val topActivities = current
//            .groupBy { it.activityName to it.activityIcon }
//            .entries
//            .sortedByDescending { entry -> entry.value.sumOf { it.elapsedSeconds } }
//            .take(3)
//            .mapIndexed { index, entry ->
//                val activitySeconds = entry.value.sumOf { it.elapsedSeconds }
//                val sharePercent =
//                    if (totalSec == 0L) 0 else ((activitySeconds * 100f) / totalSec).toInt()
//
//                TopActivityUiModel(
//                    rank = index + 1,
//                    name = entry.key.first,
//                    totalFocusTime = formatSeconds(activitySeconds),
//                    sharePercent = sharePercent,
//                    icon = entry.key.second
//                )
//            }
//
//        val weekdayOrder = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
//        val dayMap = current
//            .groupBy { session ->
//                parseDate(session.dateLabel)
//                    ?.dayOfWeek
//                    ?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//                    ?: ""
//            }
//            .mapValues { entry -> entry.value.sumOf { it.elapsedSeconds } }
//
//        val orderedDayValues = weekdayOrder.map { day ->
//            ((dayMap[day] ?: 0L) / 60L).toInt()
//        }
//
//        val bestDayEntry = dayMap.maxByOrNull { it.value }
//        val highlightedBarIndex =
//            weekdayOrder.indexOf(bestDayEntry?.key).takeIf { it >= 0 } ?: -1
//
//        _uiState.value = InsightsUiState(
//            isLoading = false,
//            selectedRange = range,
//            summary = InsightsSummary(
//                totalFocusTime = formatSeconds(totalSec),
//                totalFocusDelta = formatDelta(deltaPercent(totalSec, prevTotalSec)),
//                sessionsCompleted = completed,
//                sessionsDelta = formatDelta(deltaPercent(completed.toLong(), prevCompleted.toLong())),
//                completionRate = completionRate,
//                completionDelta = formatDelta((completionRate - prevRate).toLong())
//            ),
//            trend = TrendChartUiModel(
//                title = "Focus Time Trend",
//                totalLabel = formatSeconds(totalSec),
//                subtitle = when (range) {
//                    InsightsRange.WEEK -> "Total this week"
//                    InsightsRange.MONTH -> "Total this month"
//                    InsightsRange.YEAR -> "Total this year"
//                },
//                points = trendPoints,
//                highlightedIndex = trendPoints.indices.maxByOrNull { index ->
//                    trendPoints[index].valueMinutes
//                } ?: -1
//            ),
//            consistency = ConsistencyUiModel(
//                currentStreak = currentStreak,
//                longestStreak = longestStreak,
//                consistencyPercent = consistencyPercent,
//                completedDaysText = "${activeDays.size} of $totalDays days"
//            ),
//            topActivities = topActivities,
//            pattern = PatternInsightUiModel(
//                title = "Most Productive Day",
//                highlight = bestDayEntry?.key ?: "—",
//                value = formatSeconds(bestDayEntry?.value ?: 0L),
//                description = if (bestDayEntry != null) {
//                    "${bestDayEntry.key} is your strongest focus day."
//                } else {
//                    "Not enough data yet."
//                },
//                miniBars = orderedDayValues,
//                highlightedBarIndex = highlightedBarIndex
//            )
//        )
//    }
//
//    private fun buildTrendPoints(
//        sessions: List<SessionEntity>,
//        range: InsightsRange,
//        today: LocalDate
//    ): List<TrendPointUiModel> {
//        return when (range) {
//            InsightsRange.WEEK -> {
//                (6 downTo 0).map { daysAgo ->
//                    val day = today.minusDays(daysAgo.toLong())
//                    val label = day.format(DateTimeFormatter.ofPattern("EEE"))
//                    val minutes = sessions
//                        .filter { parseDate(it.dateLabel) == day }
//                        .sumOf { (it.elapsedSeconds / 60L).toInt() }
//
//                    TrendPointUiModel(
//                        label = label,
//                        valueMinutes = minutes
//                    )
//                }
//            }
//
//            InsightsRange.MONTH -> {
//                (3 downTo 0).map { weeksAgo ->
//                    val weekEnd = today.minusDays((weeksAgo * 7L))
//                    val weekStart = weekEnd.minusDays(6)
//                    val label = "W${4 - weeksAgo}"
//
//                    val minutes = sessions
//                        .filter { session ->
//                            val date = parseDate(session.dateLabel) ?: return@filter false
//                            !date.isBefore(weekStart) && !date.isAfter(weekEnd)
//                        }
//                        .sumOf { (it.elapsedSeconds / 60L).toInt() }
//
//                    TrendPointUiModel(
//                        label = label,
//                        valueMinutes = minutes
//                    )
//                }
//            }
//
//            InsightsRange.YEAR -> {
//                (11 downTo 0).map { monthsAgo ->
//                    val monthDate = today.minusMonths(monthsAgo.toLong())
//                    val label = monthDate.format(DateTimeFormatter.ofPattern("MMM"))
//
//                    val minutes = sessions
//                        .filter { session ->
//                            val date = parseDate(session.dateLabel) ?: return@filter false
//                            date.month == monthDate.month && date.year == monthDate.year
//                        }
//                        .sumOf { (it.elapsedSeconds / 60L).toInt() }
//
//                    TrendPointUiModel(
//                        label = label,
//                        valueMinutes = minutes
//                    )
//                }
//            }
//        }
//    }
//
//    private fun parseDate(value: String): LocalDate? {
//        return runCatching { LocalDate.parse(value) }.getOrNull()
//    }
//
//    private fun deltaPercent(current: Long, previous: Long): Long {
//        if (previous <= 0L) return 0L
//        return (((current - previous) * 100f) / previous).toLong()
//    }
//
//    private fun formatDelta(value: Long): String {
//        return if (value > 0) "+$value%" else "$value%"
//    }
//
//    private fun formatSeconds(seconds: Long): String {
//        val hours = seconds / 3600L
//        val minutes = (seconds % 3600L) / 60L
//        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
//    }
//}



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
        val currentWindow = currentWindow(range, today)
        val previousWindow = previousWindow(currentWindow)

        val datedSessions = allSessions.mapNotNull { session ->
            parseDate(session.dateLabel)?.let { date ->
                session to date
            }
        }

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

        val activeDays = current
            .map { it.second }
            .toSet()

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

        val longestStreak = calculateLongestStreak(
            days = activeDays
        )

        val trendPoints = buildTrendPoints(
            sessions = current,
            range = range,
            today = today,
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

        val weekdayOrder = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val dayMap = current
            .groupBy { (_, date) ->
                date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            }
            .mapValues { entry ->
                entry.value.sumOf { it.first.elapsedSeconds }
            }

        val orderedDayValues = weekdayOrder.map { day ->
            (dayMap[day] ?: 0L).toMinutesInt()
        }

        val bestDayEntry = dayMap
            .filterValues { it > 0L }
            .maxByOrNull { it.value }

        val highlightedBarIndex = bestDayEntry
            ?.key
            ?.let(weekdayOrder::indexOf)
            ?.takeIf { it >= 0 }
            ?: -1

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
                title = "Focus Time Trend",
                totalLabel = formatSeconds(totalSec),
                subtitle = when (range) {
                    InsightsRange.WEEK -> "Total in last 7 days"
                    InsightsRange.MONTH -> "Total in last 30 days"
                    InsightsRange.YEAR -> "Total in last 12 months"
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
            pattern = PatternInsightUiModel(
                title = "Most Productive Day",
                highlight = bestDayEntry?.key ?: "—",
                value = formatSeconds(bestDayEntry?.value ?: 0L),
                description = if (bestDayEntry != null) {
                    "${bestDayEntry.key} is your strongest focus day in this range."
                } else {
                    "Not enough data yet."
                },
                miniBars = orderedDayValues,
                highlightedBarIndex = highlightedBarIndex
            )
        )
    }

    private fun buildTrendPoints(
        sessions: List<Pair<SessionEntity, LocalDate>>,
        range: InsightsRange,
        today: LocalDate,
        currentWindow: DateWindow
    ): List<TrendPointUiModel> {
        return when (range) {
            InsightsRange.WEEK -> {
                (6 downTo 0).map { daysAgo ->
                    val day = today.minusDays(daysAgo.toLong())
                    TrendPointUiModel(
                        label = day.format(DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)),
                        valueMinutes = sessions
                            .filter { it.second == day }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }
            }

            InsightsRange.MONTH -> {
                val buckets = buildFixedDayBuckets(
                    start = currentWindow.start,
                    bucketCount = 5,
                    bucketSizeDays = 6
                )

                buckets.mapIndexed { index, bucket ->
                    TrendPointUiModel(
                        label = "D${index + 1}",
                        valueMinutes = sessions
                            .filter { (_, date) ->
                                !date.isBefore(bucket.start) && !date.isAfter(bucket.endInclusive)
                            }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }
            }

            InsightsRange.YEAR -> {
                (11 downTo 0).map { monthsAgo ->
                    val monthDate = today.minusMonths(monthsAgo.toLong())
                    TrendPointUiModel(
                        label = monthDate.format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)),
                        valueMinutes = sessions
                            .filter { (_, date) ->
                                date.year == monthDate.year && date.month == monthDate.month
                            }
                            .sumOf { it.first.elapsedSeconds }
                            .toMinutesInt()
                    )
                }
            }
        }
    }

    private fun currentWindow(range: InsightsRange, today: LocalDate): DateWindow {
        return when (range) {
            InsightsRange.WEEK -> DateWindow(
                start = today.minusDays(6),
                endInclusive = today
            )
            InsightsRange.MONTH -> DateWindow(
                start = today.minusDays(29),
                endInclusive = today
            )
            InsightsRange.YEAR -> DateWindow(
                start = today.minusYears(1).plusDays(1),
                endInclusive = today
            )
        }
    }

    private fun previousWindow(current: DateWindow): DateWindow {
        val length = ChronoUnit.DAYS.between(current.start, current.endInclusive) + 1
        return DateWindow(
            start = current.start.minusDays(length),
            endInclusive = current.start.minusDays(1)
        )
    }

    private fun buildFixedDayBuckets(
        start: LocalDate,
        bucketCount: Int,
        bucketSizeDays: Long
    ): List<DateWindow> {
        return (0 until bucketCount).map { index ->
            val bucketStart = start.plusDays(index * bucketSizeDays)
            val bucketEnd = bucketStart.plusDays(bucketSizeDays - 1)
            DateWindow(
                start = bucketStart,
                endInclusive = bucketEnd
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