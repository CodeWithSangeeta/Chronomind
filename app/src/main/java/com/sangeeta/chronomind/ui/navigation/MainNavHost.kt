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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.activities.AllActivitiesScreen
import com.sangeeta.chronomind.ui.create_activity.CreateEditActivityScreen
import com.sangeeta.chronomind.ui.history.HistoryScreen
import com.sangeeta.chronomind.ui.home.HomeScreen
import com.sangeeta.chronomind.ui.insights.InsightsScreen
import com.sangeeta.chronomind.ui.settings.SettingsScreen
import kotlin.jvm.java


private const val DURATION_ENTER = 260
private const val DURATION_EXIT = 200

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
                onNavigateToInsights       = { navController.navigate(ChronoRoutes.Insights.route) }
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
            val context = LocalContext.current
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onRowClick = { rowId ->
                    when (rowId) {
                        "privacy" -> {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(context.getString(R.string.url_privacy_policy))
                            }
                            context.startActivity(intent)
                        }

                        "terms" -> {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(context.getString(R.string.url_terms_of_service))
                            }
                            context.startActivity(intent)
                        }
                        "rateapp" -> {
                            // Try opening deep-link straight into Play Store app container first
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(context.getString(R.string.url_play_store_listing))
                            }
                            runCatching {
                                context.startActivity(intent)
                            }.onFailure {
                                // Fallback safely to web browser view if running on emulator/no store app present
                                val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                    data = android.net.Uri.parse(context.getString(R.string.url_play_store_fallback))
                                }
                                context.startActivity(webIntent)
                            }
                        }
                        "developer" -> {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(context.getString(R.string.url_developer_portfolio))
                            }
                            context.startActivity(intent)
                        }

                        "support_portal" -> {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                data = android.net.Uri.parse(context.getString(R.string.url_support_portal))
                            }
                            runCatching {
                                context.startActivity(intent)
                            }.onFailure {
                                android.widget.Toast.makeText(context, "Cannot open web browser.", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }

                        "licenses" -> {
                            runCatching {
                                val intent = android.content.Intent(context, com.google.android.gms.oss.licenses.OssLicensesMenuActivity::class.java)
                                context.startActivity(intent)
                            }.onFailure {
                                android.widget.Toast.makeText(context, "Unable to load open-source licenses", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> { }
                    }
                },
                onResetOnboarding = {
                    navController.popBackStack()
                }
            )
        }


    }
}