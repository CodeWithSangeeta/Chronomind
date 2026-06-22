package com.sangeeta.chronomind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.onboarding.OnboardingNavHost
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.activities.AllActivitiesScreen
import com.sangeeta.chronomind.ui.create_activity.CreateEditActivityScreen
import com.sangeeta.chronomind.ui.history.HistoryScreen
import com.sangeeta.chronomind.ui.home.HomeScreen
import com.sangeeta.chronomind.ui.insights.InsightsScreen
import com.sangeeta.chronomind.ui.navigation.MainNavHost
import com.sangeeta.chronomind.ui.settings.SettingsScreen
import com.sangeeta.chronomind.ui.widget_setup.WidgetSetupScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingRepository: OnboardingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDone by onboardingRepository.isOnboardingComplete
                .collectAsStateWithLifecycle(initialValue = false)


            if (isDone) {
                MainNavHost()
            } else {
                OnboardingNavHost(
                    onNavigateToMain = { }
                )
            }

//            OnboardingNavHost(
//                onNavigateToMain = {
//                    // State is driven by DataStore; recompose handles navigation
//                }
//            )

//            HomeScreen(
//                onNavigateToSettings = {},
//                onNavigateToAllActivities = {},
//                onNavigateToCreateActivity = {},
//                onNavigateToHistory = {},
//                onNavigateToInsights = {},
//                onNavigateToWidgetSetup = {},
//                onNavigateToWidgetPreview = {}
//            )

//            AllActivitiesScreen(
//                onBackClick = {},
//                onNewActivityClick = {},
//                onEditActivityClick = {},
//                onStartActivityClick = {}
//            )

//            CreateEditActivityScreen(
//                onBackClick = {},
//                onSaved = {},
//                onDeleted = {}
//            )


//            HistoryScreen(
//                onBackClick = {}
//            )

//            InsightsScreen(
//                onBackClick = {}
//            )


//            WidgetSetupScreen(
//                onBackClick = {},
//                onSetWidgetClick = {}
//            )
//
//            SettingsScreen(
//                onBackClick = {},
//                onRowClick = {}
//            )
        }
    }
}
