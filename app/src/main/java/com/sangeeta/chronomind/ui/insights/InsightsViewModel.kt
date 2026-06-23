//package com.sangeeta.chronomind.ui.insights
//
//
//import androidx.lifecycle.ViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//@HiltViewModel
//class InsightsViewModel @Inject constructor() : ViewModel() {
//
//    private val _uiState = MutableStateFlow(mockStateFor(InsightsRange.WEEK))
//    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()
//
//    fun onRangeSelected(range: InsightsRange) {
//        _uiState.update { mockStateFor(range) }
//    }
//
//    private fun mockStateFor(range: InsightsRange): InsightsUiState {
//        return when (range) {
//            InsightsRange.WEEK -> InsightsUiState(
//                selectedRange = range,
//                summary = InsightsSummary(
//                    totalFocusTime = "12h 45m",
//                    totalFocusDelta = "18%",
//                    sessionsCompleted = 18,
//                    sessionsDelta = "12%",
//                    completionRate = 82,
//                    completionDelta = "10%"
//                ),
//                trend = TrendChartUiModel(
//                    title = "Focus Time Trend",
//                    totalLabel = "12h 45m",
//                    subtitle = "Total this week",
//                    points = listOf(
//                        TrendPointUiModel("Mon", 80),
//                        TrendPointUiModel("Tue", 135),
//                        TrendPointUiModel("Wed", 105),
//                        TrendPointUiModel("Thu", 200),
//                        TrendPointUiModel("Fri", 150),
//                        TrendPointUiModel("Sat", 120),
//                        TrendPointUiModel("Sun", 75)
//                    ),
//                    highlightedIndex = 3
//                ),
//                consistency = ConsistencyUiModel(
//                    currentStreak = 7,
//                    longestStreak = 14,
//                    consistencyPercent = 78,
//                    completedDaysText = "18 of 23 days"
//                ),
//                topActivities = listOf(
//                    TopActivityUiModel(1, "Study & Learning", "5h 20m", 41, "📘"),
//                    TopActivityUiModel(2, "Workout", "3h 15m", 26, "🏋️"),
//                    TopActivityUiModel(3, "Coding Practice", "2h 40m", 21, "💻")
//                ),
//                pattern = PatternInsightUiModel(
//                    title = "Most Productive Day",
//                    highlight = "Thursday",
//                    value = "3h 20m",
//                    description = "You’re most focused on Thursdays.",
//                    miniBars = listOf(5, 7, 6, 8, 4, 6, 4),
//                    highlightedBarIndex = 3
//                )
//            )
//
//            InsightsRange.MONTH -> InsightsUiState(
//                selectedRange = range,
//                summary = InsightsSummary(
//                    totalFocusTime = "48h 20m",
//                    totalFocusDelta = "14%",
//                    sessionsCompleted = 62,
//                    sessionsDelta = "9%",
//                    completionRate = 79,
//                    completionDelta = "6%"
//                ),
//                trend = TrendChartUiModel(
//                    title = "Focus Time Trend",
//                    totalLabel = "48h 20m",
//                    subtitle = "Total this month",
//                    points = listOf(
//                        TrendPointUiModel("W1", 540),
//                        TrendPointUiModel("W2", 620),
//                        TrendPointUiModel("W3", 710),
//                        TrendPointUiModel("W4", 630)
//                    ),
//                    highlightedIndex = 2
//                ),
//                consistency = ConsistencyUiModel(
//                    currentStreak = 9,
//                    longestStreak = 17,
//                    consistencyPercent = 81,
//                    completedDaysText = "22 of 27 days"
//                ),
//                topActivities = listOf(
//                    TopActivityUiModel(1, "Study & Learning", "18h 10m", 37, "📘"),
//                    TopActivityUiModel(2, "Client Work", "10h 40m", 22, "💼"),
//                    TopActivityUiModel(3, "Workout", "8h 25m", 17, "🏋️")
//                ),
//                pattern = PatternInsightUiModel(
//                    title = "Best Focus Period",
//                    highlight = "Week 3",
//                    value = "11h 50m",
//                    description = "Your strongest focus happened in the third week.",
//                    miniBars = listOf(5, 6, 9, 7),
//                    highlightedBarIndex = 2
//                )
//            )
//
//            InsightsRange.YEAR -> InsightsUiState(
//                selectedRange = range,
//                summary = InsightsSummary(
//                    totalFocusTime = "420h 10m",
//                    totalFocusDelta = "22%",
//                    sessionsCompleted = 514,
//                    sessionsDelta = "15%",
//                    completionRate = 84,
//                    completionDelta = "8%"
//                ),
//                trend = TrendChartUiModel(
//                    title = "Focus Time Trend",
//                    totalLabel = "420h 10m",
//                    subtitle = "Total this year",
//                    points = listOf(
//                        TrendPointUiModel("Jan", 1680),
//                        TrendPointUiModel("Feb", 1540),
//                        TrendPointUiModel("Mar", 1890),
//                        TrendPointUiModel("Apr", 2010),
//                        TrendPointUiModel("May", 1760),
//                        TrendPointUiModel("Jun", 2140)
//                    ),
//                    highlightedIndex = 5
//                ),
//                consistency = ConsistencyUiModel(
//                    currentStreak = 21,
//                    longestStreak = 34,
//                    consistencyPercent = 84,
//                    completedDaysText = "143 of 170 days"
//                ),
//                topActivities = listOf(
//                    TopActivityUiModel(1, "Study & Learning", "132h 40m", 32, "📘"),
//                    TopActivityUiModel(2, "Client Work", "104h 15m", 25, "💼"),
//                    TopActivityUiModel(3, "Coding Practice", "81h 20m", 19, "💻")
//                ),
//                pattern = PatternInsightUiModel(
//                    title = "Highest Completion Month",
//                    highlight = "June",
//                    value = "91%",
//                    description = "June had your highest completion rate so far.",
//                    miniBars = listOf(5, 4, 6, 7, 6, 9),
//                    highlightedBarIndex = 5
//                )
//            )
//        }
//    }
//}



@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val activityRepo: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState(isLoading = true))
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init { observeAndBuild(InsightsRange.WEEK) }

    fun onRangeSelected(range: InsightsRange) {
        observeAndBuild(range)
    }

    private fun observeAndBuild(range: InsightsRange) {
        activityRepo.observeSessionsSince("2000-01-01")
            .onEach { sessions -> buildInsights(sessions, range) }
            .launchIn(viewModelScope)
    }

    private fun buildInsights(all: List<SessionEntity>, range: InsightsRange) {
        val today = LocalDate.now()
        val cutoff = when (range) {
            InsightsRange.WEEK  -> today.minusDays(6)
            InsightsRange.MONTH -> today.minusDays(29)
            InsightsRange.YEAR  -> today.minusYears(1)
        }

        val current = all.filter { !LocalDate.parse(it.dateLabel).isBefore(cutoff) }
        val previous = all.filter {
            val d = LocalDate.parse(it.dateLabel)
            d.isBefore(cutoff) && !d.isBefore(cutoff.minusDays(cutoff.until(today, ChronoUnit.DAYS)))
        }

        // Summary
        val totalSec     = current.sumOf { it.elapsedSeconds }
        val prevTotalSec = previous.sumOf { it.elapsedSeconds }
        val completed    = current.count { it.isCompleted }
        val prevCompleted = previous.count { it.isCompleted }
        val completionRate = if (current.isEmpty()) 0 else (completed * 100f / current.size).toInt()
        val prevRate = if (previous.isEmpty()) 0 else (prevCompleted * 100f / previous.size).toInt()

        // Trend chart — group by day/week label
        val trendPoints = buildTrendPoints(current, range, today)

        // Consistency
        val activeDays = current.map { it.dateLabel }.toSet()
        val totalDays  = cutoff.until(today, ChronoUnit.DAYS).toInt() + 1
        val consistency = if (totalDays == 0) 0 else (activeDays.size * 100f / totalDays).toInt()

        // Current streak — count back from today
        var streak = 0
        var checkDay = today
        while (activeDays.contains(checkDay.toString())) {
            streak++
            checkDay = checkDay.minusDays(1)
        }

        // Longest streak
        val sortedDays = all.map { LocalDate.parse(it.dateLabel) }.toSortedSet()
        var longest = 0; var tempStreak = 0; var prev: LocalDate? = null
        for (day in sortedDays) {
            tempStreak = if (prev != null && prev!!.plusDays(1) == day) tempStreak + 1 else 1
            if (tempStreak > longest) longest = tempStreak
            prev = day
        }

        // Top activities by total time
        val topActivities = current
            .groupBy { it.activityName to it.activityIcon }
            .entries
            .sortedByDescending { it.value.sumOf { s -> s.elapsedSeconds } }
            .take(3)
            .mapIndexed { i, entry ->
                val secs = entry.value.sumOf { it.elapsedSeconds }
                val share = if (totalSec == 0L) 0 else (secs * 100f / totalSec).toInt()
                TopActivityUiModel(
                    rank          = i + 1,
                    name          = entry.key.first,
                    totalFocusTime = formatSeconds(secs),
                    sharePercent  = share,
                    emoji         = entry.key.second
                )
            }

        // Pattern insight — most productive day of week
        val dayMap = current.groupBy {
            LocalDate.parse(it.dateLabel).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }.mapValues { e -> e.value.sumOf { it.elapsedSeconds } }
        val bestDay = dayMap.maxByOrNull { it.value }

        _uiState.value = InsightsUiState(
            isLoading      = false,
            selectedRange  = range,
            summary        = InsightsSummary(
                totalFocusTime    = formatSeconds(totalSec),
                totalFocusDelta   = deltaPercent(totalSec, prevTotalSec),
                sessionsCompleted = completed,
                sessionsDelta     = deltaPercent(completed.toLong(), prevCompleted.toLong()),
                completionRate    = completionRate,
                completionDelta   = completionRate - prevRate
            ),
            trend          = TrendChartUiModel(
                title          = "Focus Time Trend",
                totalLabel     = formatSeconds(totalSec),
                subtitle       = "Total this ${range.name.lowercase()}",
                points         = trendPoints,
                highlightedIndex = trendPoints.indices.maxByOrNull { trendPoints[it].valueMinutes } ?: 0
            ),
            consistency    = ConsistencyUiModel(
                currentStreak       = streak,
                longestStreak       = longest,
                consistencyPercent  = consistency,
                completedDaysText   = "${activeDays.size} of $totalDays days"
            ),
            topActivities  = topActivities,
            pattern        = PatternInsightUiModel(
                title              = "Most Productive Day",
                highlight          = bestDay?.key ?: "—",
                value              = formatSeconds(bestDay?.value ?: 0L),
                description        = if (bestDay != null) "${bestDay.key} is your strongest focus day." else "Not enough data yet.",
                miniBars           = dayMap.values.map { (it / 60).toInt() }.takeLast(7),
                highlightedBarIndex = dayMap.values.toList().indexOfLast { it == bestDay?.value }
            )
        )
    }

    private fun buildTrendPoints(sessions: List<SessionEntity>, range: InsightsRange, today: LocalDate): List<TrendPointUiModel> {
        return when (range) {
            InsightsRange.WEEK -> (6 downTo 0).map { daysAgo ->
                val day = today.minusDays(daysAgo.toLong())
                val label = day.format(DateTimeFormatter.ofPattern("EEE"))
                val mins = sessions.filter { it.dateLabel == day.toString() }.sumOf { it.elapsedSeconds / 60 }.toInt()
                TrendPointUiModel(label, mins)
            }
            InsightsRange.MONTH -> (3 downTo 0).map { weeksAgo ->
                val weekStart = today.minusDays(weeksAgo * 7L)
                val label = "W${4 - weeksAgo}"
                val mins = sessions.filter {
                    val d = LocalDate.parse(it.dateLabel)
                    !d.isBefore(weekStart.minusDays(6)) && !d.isAfter(weekStart)
                }.sumOf { it.elapsedSeconds / 60 }.toInt()
                TrendPointUiModel(label, mins)
            }
            InsightsRange.YEAR -> (11 downTo 0).map { monthsAgo ->
                val month = today.minusMonths(monthsAgo.toLong())
                val label = month.format(DateTimeFormatter.ofPattern("MMM"))
                val mins = sessions.filter {
                    LocalDate.parse(it.dateLabel).month == month.month &&
                            LocalDate.parse(it.dateLabel).year == month.year
                }.sumOf { it.elapsedSeconds / 60 }.toInt()
                TrendPointUiModel(label, mins)
            }
        }
    }

    private fun deltaPercent(current: Long, previous: Long): Int {
        if (previous == 0L) return 0
        return ((current - previous) * 100f / previous).toInt()
    }

    private fun formatSeconds(sec: Long): String {
        val h = sec / 3600; val m = (sec % 3600) / 60
        return if (h > 0) "${h}h ${m}m" else "${m}m"
    }
}