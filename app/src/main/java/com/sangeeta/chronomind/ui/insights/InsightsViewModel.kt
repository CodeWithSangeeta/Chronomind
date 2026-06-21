package com.sangeeta.chronomind.ui.insights


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class InsightsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(mockStateFor(InsightsRange.WEEK))
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    fun onRangeSelected(range: InsightsRange) {
        _uiState.update { mockStateFor(range) }
    }

    private fun mockStateFor(range: InsightsRange): InsightsUiState {
        return when (range) {
            InsightsRange.WEEK -> InsightsUiState(
                selectedRange = range,
                summary = InsightsSummary(
                    totalFocusTime = "12h 45m",
                    totalFocusDelta = "18%",
                    sessionsCompleted = 18,
                    sessionsDelta = "12%",
                    completionRate = 82,
                    completionDelta = "10%"
                ),
                trend = TrendChartUiModel(
                    title = "Focus Time Trend",
                    totalLabel = "12h 45m",
                    subtitle = "Total this week",
                    points = listOf(
                        TrendPointUiModel("Mon", 80),
                        TrendPointUiModel("Tue", 135),
                        TrendPointUiModel("Wed", 105),
                        TrendPointUiModel("Thu", 200),
                        TrendPointUiModel("Fri", 150),
                        TrendPointUiModel("Sat", 120),
                        TrendPointUiModel("Sun", 75)
                    ),
                    highlightedIndex = 3
                ),
                consistency = ConsistencyUiModel(
                    currentStreak = 7,
                    longestStreak = 14,
                    consistencyPercent = 78,
                    completedDaysText = "18 of 23 days"
                ),
                topActivities = listOf(
                    TopActivityUiModel(1, "Study & Learning", "5h 20m", 41, "📘"),
                    TopActivityUiModel(2, "Workout", "3h 15m", 26, "🏋️"),
                    TopActivityUiModel(3, "Coding Practice", "2h 40m", 21, "💻")
                ),
                pattern = PatternInsightUiModel(
                    title = "Most Productive Day",
                    highlight = "Thursday",
                    value = "3h 20m",
                    description = "You’re most focused on Thursdays.",
                    miniBars = listOf(5, 7, 6, 8, 4, 6, 4),
                    highlightedBarIndex = 3
                )
            )

            InsightsRange.MONTH -> InsightsUiState(
                selectedRange = range,
                summary = InsightsSummary(
                    totalFocusTime = "48h 20m",
                    totalFocusDelta = "14%",
                    sessionsCompleted = 62,
                    sessionsDelta = "9%",
                    completionRate = 79,
                    completionDelta = "6%"
                ),
                trend = TrendChartUiModel(
                    title = "Focus Time Trend",
                    totalLabel = "48h 20m",
                    subtitle = "Total this month",
                    points = listOf(
                        TrendPointUiModel("W1", 540),
                        TrendPointUiModel("W2", 620),
                        TrendPointUiModel("W3", 710),
                        TrendPointUiModel("W4", 630)
                    ),
                    highlightedIndex = 2
                ),
                consistency = ConsistencyUiModel(
                    currentStreak = 9,
                    longestStreak = 17,
                    consistencyPercent = 81,
                    completedDaysText = "22 of 27 days"
                ),
                topActivities = listOf(
                    TopActivityUiModel(1, "Study & Learning", "18h 10m", 37, "📘"),
                    TopActivityUiModel(2, "Client Work", "10h 40m", 22, "💼"),
                    TopActivityUiModel(3, "Workout", "8h 25m", 17, "🏋️")
                ),
                pattern = PatternInsightUiModel(
                    title = "Best Focus Period",
                    highlight = "Week 3",
                    value = "11h 50m",
                    description = "Your strongest focus happened in the third week.",
                    miniBars = listOf(5, 6, 9, 7),
                    highlightedBarIndex = 2
                )
            )

            InsightsRange.YEAR -> InsightsUiState(
                selectedRange = range,
                summary = InsightsSummary(
                    totalFocusTime = "420h 10m",
                    totalFocusDelta = "22%",
                    sessionsCompleted = 514,
                    sessionsDelta = "15%",
                    completionRate = 84,
                    completionDelta = "8%"
                ),
                trend = TrendChartUiModel(
                    title = "Focus Time Trend",
                    totalLabel = "420h 10m",
                    subtitle = "Total this year",
                    points = listOf(
                        TrendPointUiModel("Jan", 1680),
                        TrendPointUiModel("Feb", 1540),
                        TrendPointUiModel("Mar", 1890),
                        TrendPointUiModel("Apr", 2010),
                        TrendPointUiModel("May", 1760),
                        TrendPointUiModel("Jun", 2140)
                    ),
                    highlightedIndex = 5
                ),
                consistency = ConsistencyUiModel(
                    currentStreak = 21,
                    longestStreak = 34,
                    consistencyPercent = 84,
                    completedDaysText = "143 of 170 days"
                ),
                topActivities = listOf(
                    TopActivityUiModel(1, "Study & Learning", "132h 40m", 32, "📘"),
                    TopActivityUiModel(2, "Client Work", "104h 15m", 25, "💼"),
                    TopActivityUiModel(3, "Coding Practice", "81h 20m", 19, "💻")
                ),
                pattern = PatternInsightUiModel(
                    title = "Highest Completion Month",
                    highlight = "June",
                    value = "91%",
                    description = "June had your highest completion rate so far.",
                    miniBars = listOf(5, 4, 6, 7, 6, 9),
                    highlightedBarIndex = 5
                )
            )
        }
    }
}