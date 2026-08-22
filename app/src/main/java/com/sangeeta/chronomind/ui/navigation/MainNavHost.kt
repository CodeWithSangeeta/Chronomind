package com.sangeeta.chronomind.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
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
import com.sangeeta.chronomind.ui.settings.OpenSourceLicensesScreen
import com.sangeeta.chronomind.ui.settings.SettingsScreen
import androidx.core.net.toUri


private const val NAVIGATION_DURATION = 180

private val defaultEnter:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {

    slideInHorizontally(
        initialOffsetX = { fullWidth ->
            fullWidth
        },
        animationSpec = tween(
            durationMillis = NAVIGATION_DURATION,
            easing = FastOutSlowInEasing
        )
    )
}


private val defaultExit:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {

    ExitTransition.None
}
private val defaultPopEnter:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {

    slideInHorizontally(
        initialOffsetX = { fullWidth ->
            -fullWidth
        },
        animationSpec = tween(
            durationMillis = NAVIGATION_DURATION,
            easing = FastOutSlowInEasing
        )
    )
}

private val defaultPopExit:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {

    slideOutHorizontally(
        targetOffsetX = { fullWidth ->
            fullWidth
        },
        animationSpec = tween(
            durationMillis = NAVIGATION_DURATION,
            easing = FastOutSlowInEasing
        )
    )
}

private val sheetEnter:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {

    slideInVertically(
        initialOffsetY = { fullHeight ->
            fullHeight / 3
        },
        animationSpec = tween(
            NAVIGATION_DURATION,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(
        animationSpec = tween(NAVIGATION_DURATION)
    )
}


private val sheetExit:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {

    fadeOut(
        animationSpec = tween(NAVIGATION_DURATION)
    )
}


private val sheetPopEnter:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {

    fadeIn(
        animationSpec = tween(NAVIGATION_DURATION)
    )
}


private val sheetPopExit:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {

    slideOutVertically(
        targetOffsetY = { fullHeight ->
            fullHeight / 3
        },
        animationSpec = tween(
            NAVIGATION_DURATION,
            easing = FastOutSlowInEasing
        )
    ) + fadeOut(
        animationSpec = tween(NAVIGATION_DURATION)
    )
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

        composable(
            route = ChronoRoutes.Home.route
        ) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(
                        ChronoRoutes.Settings.route
                    )
                },

                onNavigateToAllActivities = {
                    navController.navigate(
                        ChronoRoutes.AllActivities.route
                    )
                },

                onNavigateToCreateActivity = {
                    navController.navigate(
                        ChronoRoutes.CreateEditActivity.createRoute()
                    )
                },

                onNavigateToHistory = {
                    navController.navigate(
                        ChronoRoutes.History.route
                    )
                },

                onNavigateToInsights = {
                    navController.navigate(
                        ChronoRoutes.Insights.route
                    )
                }
            )
        }

        composable(
            route = ChronoRoutes.AllActivities.route
        ) {
            AllActivitiesScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onNewActivityClick = {
                    navController.navigate(
                        ChronoRoutes.CreateEditActivity.createRoute()
                    )
                },

                onEditActivityClick = { id ->
                    navController.navigate(
                        ChronoRoutes.CreateEditActivity.createRoute(id)
                    )
                },

                onSelectActivityClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ChronoRoutes.CreateEditActivity.route,

            arguments = listOf(
                navArgument(
                    ChronoRoutes.CreateEditActivity.ARG
                ) {
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
                onBackClick = {
                    navController.popBackStack()
                },

                onNavigateBack = {
                    navController.popBackStack()
                },

                onNavigateHomeAfterStart = {

                    navController.navigate(
                        ChronoRoutes.Home.route
                    ) {
                        popUpTo(
                            ChronoRoutes.Home.route
                        ) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = ChronoRoutes.History.route
        ) {
            HistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ChronoRoutes.Insights.route
        ) {
            InsightsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ChronoRoutes.Settings.route
        ) {

            val context = LocalContext.current

            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onRowClick = { rowId ->

                    when (rowId) {

                        "privacy" -> {
                            val intent =
                                android.content.Intent(
                                    android.content.Intent.ACTION_VIEW
                                ).apply {

                                    data = context.getString(
                                        R.string.url_privacy_policy
                                    ).toUri()
                                }

                            context.startActivity(intent)
                        }


                        "terms" -> {
                            val intent =
                                android.content.Intent(
                                    android.content.Intent.ACTION_VIEW
                                ).apply {

                                    data =
                                        context.getString(
                                            R.string.url_terms_of_service
                                        ).toUri()
                                }

                            context.startActivity(intent)
                        }


                        "developer" -> {
                            val intent =
                                android.content.Intent(
                                    android.content.Intent.ACTION_VIEW
                                ).apply {

                                    data =
                                        context.getString(
                                            R.string.url_developer_portfolio
                                        ).toUri()
                                }

                            context.startActivity(intent)
                        }


                        "support_portal" -> {
                            val intent =
                                android.content.Intent(
                                    android.content.Intent.ACTION_VIEW
                                ).apply {

                                    data =
                                        context.getString(
                                                R.string.url_support_portal
                                        ).toUri()
                                }

                            runCatching {
                            context.startActivity(intent)
                            }.onFailure {
                                android.widget.Toast.makeText(
                                    context,
                                    "Cannot open web browser.",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                        "licenses" -> {
                            navController.navigate(
                                ChronoRoutes.OpenSourceLicenses.route
                            )
                        }


                        else -> {}
                    }
                },

                onResetOnboarding = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ChronoRoutes.OpenSourceLicenses.route
        ) {
            OpenSourceLicensesScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}