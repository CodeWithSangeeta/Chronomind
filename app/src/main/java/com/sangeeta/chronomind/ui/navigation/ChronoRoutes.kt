package com.sangeeta.chronomind.ui.navigation

sealed class ChronoRoutes(val route: String) {
    data object Home : ChronoRoutes("home")
    data object AllActivities : ChronoRoutes("all_activities")
    data object History : ChronoRoutes("history")
    data object Insights : ChronoRoutes("insights")
    data object Settings : ChronoRoutes("settings")
    data object WidgetSetup : ChronoRoutes("widget_setup")

    data object CreateEditActivity : ChronoRoutes("create_edit_activity/{activityId}") {
        const val ARG = "activityId"
        fun createRoute(activityId: Int = -1) = "create_edit_activity/$activityId"
    }
}