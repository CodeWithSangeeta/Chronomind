//package com.sangeeta.chronomind.ui.navigation
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.sangeeta.chronomind.ui.home.HomeScreen
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//
//@Composable
//fun MainNavHost(
//    navController: NavHostController = rememberNavController()
//) {
//    NavHost(
//        navController = navController,
//        startDestination = ChronoRoutes.HOME
//    ) {
//        composable(ChronoRoutes.HOME) {
//            HomeScreen(
//                onNavigateToSettings = {
//                    navController.navigate(ChronoRoutes.SETTINGS)
//                },
//                onNavigateToAllActivities = {
//                    navController.navigate(ChronoRoutes.ALL_ACTIVITIES)
//                },
//                onNavigateToCreateActivity = {
//                    navController.navigate(ChronoRoutes.CREATE_ACTIVITY)
//                },
//                onNavigateToHistory = {
//                    navController.navigate(ChronoRoutes.HISTORY)
//                },
//                onNavigateToInsights = {
//                    navController.navigate(ChronoRoutes.INSIGHTS)
//                },
//                onNavigateToWidgetSetup = {
//                    navController.navigate(ChronoRoutes.WIDGET_SETUP)
//                },
//                onNavigateToWidgetPreview = {
//                    navController.navigate(ChronoRoutes.WIDGET_PREVIEW)
//                }
//            )
//        }
//
//        composable(ChronoRoutes.ALL_ACTIVITIES) {
//            PlaceholderScreen(title = "All Activities")
//        }
//
//        composable(ChronoRoutes.CREATE_ACTIVITY) {
//            PlaceholderScreen(title = "Create Activity")
//        }
//
//        composable(ChronoRoutes.HISTORY) {
//            PlaceholderScreen(title = "History")
//        }
//
//        composable(ChronoRoutes.INSIGHTS) {
//            PlaceholderScreen(title = "Insights")
//        }
//
//        composable(ChronoRoutes.SETTINGS) {
//            PlaceholderScreen(title = "Settings")
//        }
//
//        composable(ChronoRoutes.WIDGET_SETUP) {
//            PlaceholderScreen(title = "Widget Setup")
//        }
//
//        composable(ChronoRoutes.WIDGET_PREVIEW) {
//            PlaceholderScreen(title = "Widget Preview")
//        }
//    }
//}
//
//@Composable
//private fun PlaceholderScreen(title: String) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = title,
//            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
//            color = AuraColors.TextPrimary
//        )
//    }
//}


package com.sangeeta.chronomind.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sangeeta.chronomind.ui.activities.AllActivitiesScreen
import com.sangeeta.chronomind.ui.create_activity.CreateEditActivityScreen
import com.sangeeta.chronomind.ui.history.HistoryScreen
import com.sangeeta.chronomind.ui.home.HomeScreen
import com.sangeeta.chronomind.ui.insights.InsightsScreen
import com.sangeeta.chronomind.ui.settings.SettingsScreen
import com.sangeeta.chronomind.ui.widget_setup.WidgetSetupScreen

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ChronoRoutes.Home.route
    ) {

        // ── Home ────────────────────────────────────────────────────────
        composable(route = ChronoRoutes.Home.route) {
            HomeScreen(
                onNavigateToSettings       = { navController.navigate(ChronoRoutes.Settings.route) },
                onNavigateToAllActivities  = { navController.navigate(ChronoRoutes.AllActivities.route) },
                onNavigateToCreateActivity = { navController.navigate(ChronoRoutes.CreateEditActivity.createRoute()) },
                onNavigateToHistory        = { navController.navigate(ChronoRoutes.History.route) },
                onNavigateToInsights       = { navController.navigate(ChronoRoutes.Insights.route) },
                onNavigateToWidgetSetup    = { navController.navigate(ChronoRoutes.WidgetSetup.route) },
                onNavigateToWidgetPreview  = { navController.navigate(ChronoRoutes.WidgetSetup.route) }
            )
        }

        // ── All Activities ──────────────────────────────────────────────
//        composable(route = ChronoRoutes.AllActivities.route) {
//            AllActivitiesScreen(
//                onBackClick          = { navController.popBackStack() },
//                onNewActivityClick   = { navController.navigate(ChronoRoutes.CreateEditActivity.createRoute()) },
//                onEditActivityClick  = { id -> navController.navigate(ChronoRoutes.CreateEditActivity.createRoute(id)) },
//                onStartActivityClick = { navController.popBackStack() }
//            )
//        }

        composable(route = ChronoRoutes.AllActivities.route) {
            AllActivitiesScreen(
                onBackClick = { navController.popBackStack() },
                onNewActivityClick = {
                    navController.navigate(ChronoRoutes.CreateEditActivity.createRoute())
                },
                onEditActivityClick = { id ->
                    navController.navigate(ChronoRoutes.CreateEditActivity.createRoute(id))
                },
                onSelectActivityClick = {
                    navController.popBackStack()
                }
            )
        }

        // ── Create / Edit Activity ──────────────────────────────────────
        composable(
            route = ChronoRoutes.CreateEditActivity.route,
            arguments = listOf(
                navArgument(ChronoRoutes.CreateEditActivity.ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            CreateEditActivityScreen(
                onBackClick = { navController.popBackStack() },
                onSaved     = { navController.popBackStack() },
                onDeleted   = { navController.popBackStack() }
            )
        }

        // ── History ─────────────────────────────────────────────────────
        composable(route = ChronoRoutes.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ── Insights ────────────────────────────────────────────────────
        composable(route = ChronoRoutes.Insights.route) {
            InsightsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ── Settings ────────────────────────────────────────────────────
        composable(route = ChronoRoutes.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ── Widget Setup ─────────────────────────────────────────────────
        composable(route = ChronoRoutes.WidgetSetup.route) {
//            WidgetSetupScreen(
//                onBackClick = { navController.popBackStack() }
//            )
        }
    }
}