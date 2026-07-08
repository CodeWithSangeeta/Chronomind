package com.sangeeta.chronomind.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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

// ---------- Shared transition specs ----------

private const val DURATION_ENTER = 260
private const val DURATION_EXIT = 200

// Forward push: new screen fades+scales in, old screen fades+scales out slightly
private val defaultEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(DURATION_ENTER, easing = FastOutSlowInEasing)) +
            scaleIn(
                initialScale = 0.96f,
                animationSpec = tween(DURATION_ENTER, easing = FastOutSlowInEasing)
            )
}

private val defaultExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(DURATION_EXIT)) +
            scaleOut(
                targetScale = 1.04f,
                animationSpec = tween(DURATION_EXIT)
            )
}

// Back/pop: reverse feel, returning screen scales down from 1.04 -> 1, old scales up slightly
private val defaultPopEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(DURATION_ENTER, easing = FastOutSlowInEasing)) +
            scaleIn(
                initialScale = 1.04f,
                animationSpec = tween(DURATION_ENTER, easing = FastOutSlowInEasing)
            )
}

private val defaultPopExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(DURATION_EXIT)) +
            scaleOut(
                targetScale = 0.96f,
                animationSpec = tween(DURATION_EXIT)
            )
}

// Sheet-style transitions for Create/Edit Activity (feels like a modal, not a lateral push)
private val sheetEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight / 3 },
        animationSpec = tween(DURATION_ENTER, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(DURATION_ENTER))
}

private val sheetExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(DURATION_EXIT))
}

private val sheetPopEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(DURATION_ENTER))
}

private val sheetPopExit: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight / 3 },
        animationSpec = tween(DURATION_EXIT, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(DURATION_EXIT))
}

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ChronoRoutes.Home.route,
        enterTransition = defaultEnter,
        exitTransition = defaultExit,
        popEnterTransition = defaultPopEnter,
        popExitTransition = defaultPopExit
    ) {

        composable(route = ChronoRoutes.Home.route) {
            HomeScreen(
                onNavigateToSettings       = { navController.navigate(ChronoRoutes.Settings.route) },
                onNavigateToAllActivities  = { navController.navigate(ChronoRoutes.AllActivities.route) },
                onNavigateToCreateActivity = { navController.navigate(ChronoRoutes.CreateEditActivity.createRoute()) },
                onNavigateToHistory        = { navController.navigate(ChronoRoutes.History.route) },
                onNavigateToInsights       = { navController.navigate(ChronoRoutes.Insights.route) },
                onNavigateToWidgetSetup    = { navController.navigate(ChronoRoutes.WidgetSetup.route) },
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

        // Sheet-style transition override for this destination only
        composable(
            route = ChronoRoutes.CreateEditActivity.route,
            arguments = listOf(
                navArgument(ChronoRoutes.CreateEditActivity.ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            enterTransition = sheetEnter,
            exitTransition = sheetExit,
            popEnterTransition = sheetPopEnter,
            popExitTransition = sheetPopExit
        ) {
            CreateEditActivityScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() },
                onNavigateHomeAfterStart = {
                    navController.navigate(ChronoRoutes.Home.route) {
                        popUpTo(ChronoRoutes.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
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

        composable(route = ChronoRoutes.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onRowClick = { rowId ->
                    when (rowId) {
                        "widgetsetup" -> { /* navController.navigate(ChronoRoutes.WidgetSetup.route) */ }
                        "helpcenter"  -> { /* open help url */ }
                        "shareapp"    -> { /* share intent */ }
                        "rateapp"     -> { /* open play store */ }
                        "privacy"     -> { /* open privacy url */ }
                        "terms"       -> { /* open terms url */ }
                        "permissions" -> { /* open app settings */ }
                        "developer"   -> { /* open portfolio/linkedin */ }
                        else          -> { }
                    }
                },
                onResetOnboarding = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = ChronoRoutes.WidgetSetup.route) {
            // TODO: WidgetSetupScreen
        }
    }
}