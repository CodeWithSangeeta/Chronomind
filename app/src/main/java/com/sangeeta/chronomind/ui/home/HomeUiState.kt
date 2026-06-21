package com.sangeeta.chronomind.ui.home


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.chronomind.ui.model.ActivityUiModel

data class HomeUiState(
    val isLoading: Boolean = true,
    val appName: String = "ChronoMind",
    val subtitle: String = "Focus • Track • Grow",
    val selectedActivity: ActivityUiModel? = null,
    val runningActivity: ActivityUiModel? = null,
    val recentActivities: List<ActivityUiModel> = emptyList(),
    val quickActions: List<HomeQuickAction> = HomeQuickAction.defaultActions()
)

data class HomeQuickAction(
    val id: String,
    val title: String,
    val icon: ImageVector
) {
    companion object {
        fun defaultActions(): List<HomeQuickAction> = listOf(
            HomeQuickAction(
                id = "new_activity",
                title = "New Activity",
                icon = Icons.Rounded.Add
            ),
            HomeQuickAction(
                id = "history",
                title = "History",
                icon = Icons.Rounded.History
            ),
            HomeQuickAction(
                id = "insights",
                title = "Insights",
                icon = Icons.Rounded.BarChart
            ),
            HomeQuickAction(
                id = "widget_setup",
                title = "Widget Setup",
                icon = Icons.Rounded.Widgets
            ),
            HomeQuickAction(
                id = "widget_preview",
                title = "Widget Preview",
                icon = Icons.Rounded.PhoneAndroid
            )
        )
    }
}