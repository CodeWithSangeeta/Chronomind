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

        composable(route = ChronoRoutes.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }


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
//            SettingsScreen(
//                onBackClick = { navController.popBackStack() },
//                onResetOnboarding = {
//                    // Navigate to onboarding root and clear back stack
//                   // navController.navigate(ChronoRoutes.Onboarding.route) {
//                     //   popUpTo(0) { inclusive = true }
//                    }
//            )
        }

        // ── Widget Setup ─────────────────────────────────────────────────
        composable(route = ChronoRoutes.WidgetSetup.route) {
//            WidgetSetupScreen(
//                onBackClick = { navController.popBackStack() }
//            )
        }
    }
}