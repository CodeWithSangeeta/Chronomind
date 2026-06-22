//package com.sangeeta.chronomind.ui.navigation
//
//object ChronoRoutes {
//    const val HOME              = "home"
//    const val ALL_ACTIVITIES    = "all_activities"
//    const val CREATE_ACTIVITY   = "create_activity"
//    const val EDIT_ACTIVITY     = "edit_activity/{activityId}"
//    const val HISTORY           = "history"
//    const val INSIGHTS          = "insights"
//    const val SETTINGS          = "settings"
//    const val WIDGET_SETUP      = "widget_setup"
//    const val WIDGET_PREVIEW      = "widget_preview"
//
//    // helper for building the edit route with actual id
//    fun editActivity(activityId: Int) = "edit_activity/$activityId"
//}


package com.sangeeta.chronomind.ui.navigation

sealed class ChronoRoutes(val route: String) {

    // ── Main app (post-onboarding) ──────────────────────────────────────
    data object Home : ChronoRoutes("home")
    data object AllActivities : ChronoRoutes("all_activities")
    data object History : ChronoRoutes("history")
    data object Insights : ChronoRoutes("insights")
    data object Settings : ChronoRoutes("settings")
    data object WidgetSetup : ChronoRoutes("widget_setup")

    // ── Create / Edit  (activityId = -1 means Create mode) ─────────────
    data object CreateEditActivity : ChronoRoutes("create_edit_activity/{activityId}") {
        const val ARG = "activityId"
        fun createRoute(activityId: Int = -1) = "create_edit_activity/$activityId"
    }
}