package com.sangeeta.chronomind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sangeeta.chronomind.ui.home.HomeScreen
import com.sangeeta.chronomind.ui.onboarding.OnboardingNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnboardingNavHost(
                onNavigateToMain = {
                    // later: navigate to dashboard/home graph
                }
            )

//            HomeScreen(
//                onCreateActivityClick = { },
//                onActivityClick = { },
//                onCurrentFocusClick = { },
//                onHistoryClick = { },
//                onInsightsClick = { },
//                onSettingsClick = { }
//            )


       }
    }
}
