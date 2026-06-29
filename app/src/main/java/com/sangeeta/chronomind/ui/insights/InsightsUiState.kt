//package com.sangeeta.chronomind.ui.insights
//
//import androidx.compose.ui.graphics.vector.ImageVector
//
//data class InsightsUiState(
//    val isLoading: Boolean = false,
//    val selectedRange: InsightsRange = InsightsRange.WEEK,
//    val summary: InsightsSummary = InsightsSummary(),
//    val trend: TrendChartUiModel = TrendChartUiModel(),
//    val consistency: ConsistencyUiModel = ConsistencyUiModel(),
//    val topActivities: List<TopActivityUiModel> = emptyList(),
//    val pattern: PatternInsightUiModel = PatternInsightUiModel()
//)
//
//enum class InsightsRange(val label: String) {
//    WEEK("Week"),
//    MONTH("Month"),
//    YEAR("Year")
//}
//
//data class InsightsSummary(
//    val totalFocusTime: String = "0m",
//    val totalFocusDelta: String = "0%",
//    val sessionsCompleted: Int = 0,
//    val sessionsDelta: String = "0%",
//    val completionRate: Int = 0,
//    val completionDelta: String = "0%"
//)
//
//data class TrendChartUiModel(
//    val title: String = "Focus Time Trend",
//    val totalLabel: String = "0m",
//    val subtitle: String = "Total this week",
//    val points: List<TrendPointUiModel> = emptyList(),
//    val highlightedIndex: Int = -1
//)
//
//data class TrendPointUiModel(
//    val label: String,
//    val valueMinutes: Int
//)
//
//data class ConsistencyUiModel(
//    val currentStreak: Int = 0,
//    val longestStreak: Int = 0,
//    val consistencyPercent: Int = 0,
//    val completedDaysText: String = "0 of 0 days"
//)
//
//data class TopActivityUiModel(
//    val rank: Int,
//    val name: String,
//    val totalFocusTime: String,
//    val sharePercent: Int,
//    val icon: ImageVector
//)
//
//data class PatternInsightUiModel(
//    val title: String = "Most Productive Day",
//    val highlight: String = "",
//    val value: String = "",
//    val description: String = "",
//    val miniBars: List<Int> = emptyList(),
//    val highlightedBarIndex: Int = -1
//)






package com.sangeeta.chronomind.ui.insights

import androidx.compose.ui.graphics.vector.ImageVector

data class InsightsUiState(
    val isLoading: Boolean = false,
    val selectedRange: InsightsRange = InsightsRange.WEEK,
    val summary: InsightsSummary = InsightsSummary(),
    val trend: TrendChartUiModel = TrendChartUiModel(),
    val consistency: ConsistencyUiModel = ConsistencyUiModel(),
    val topActivities: List<TopActivityUiModel> = emptyList(),
    val pattern: PatternInsightUiModel = PatternInsightUiModel()
)

enum class InsightsRange(val label: String) {
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year")
}

data class InsightsSummary(
    val totalFocusTime: String = "0m",
    val totalFocusDelta: String = "0%",
    val sessionsCompleted: Int = 0,
    val sessionsDelta: String = "0%",
    val completionRate: Int = 0,
    val completionDelta: String = "0pp"
)

data class TrendChartUiModel(
    val title: String = "Focus Time Trend",
    val totalLabel: String = "0m",
    val subtitle: String = "Total this week",
    val points: List<TrendPointUiModel> = emptyList(),
    val highlightedIndex: Int = -1
)

data class TrendPointUiModel(
    val label: String,
    val valueMinutes: Int
)

data class ConsistencyUiModel(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val consistencyPercent: Int = 0,
    val completedDaysText: String = "0 of 0 days"
)

data class TopActivityUiModel(
    val rank: Int,
    val name: String,
    val totalFocusTime: String,
    val sharePercent: Int,
    val icon: ImageVector
)

data class PatternInsightUiModel(
    val title: String = "Most Productive Day",
    val highlight: String = "",
    val value: String = "",
    val description: String = "",
    val miniBars: List<Int> = emptyList(),
    val highlightedBarIndex: Int = -1
)