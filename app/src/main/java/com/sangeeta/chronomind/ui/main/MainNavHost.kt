package com.sangeeta.chronomind.ui.main


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sangeeta.chronomind.ui.history.HistoryScreen
import com.sangeeta.chronomind.ui.home.HomeScreen
import com.sangeeta.chronomind.ui.insights.InsightsScreen
import com.sangeeta.chronomind.ui.newactivity.NewActivityScreen


sealed class MainRoute(val route: String) {
    object Home        : MainRoute("home")
    object NewActivity : MainRoute("new_activity")
    object History     : MainRoute("history")
    object Insights    : MainRoute("insights")
}

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController  = navController,
        startDestination = MainRoute.Home.route
    ) {
        composable(MainRoute.Home.route) {
            HomeScreen(
                onNewActivity = { navController.navigate(MainRoute.NewActivity.route) },
                onHistory     = { navController.navigate(MainRoute.History.route) },
                onInsights    = { navController.navigate(MainRoute.Insights.route) }
            )
        }
        composable(MainRoute.NewActivity.route) {
            NewActivityScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainRoute.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainRoute.Insights.route) {
            InsightsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}