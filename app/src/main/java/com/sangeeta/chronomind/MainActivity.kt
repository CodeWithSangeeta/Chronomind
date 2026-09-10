package com.sangeeta.chronomind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.onboarding.OnboardingNavHost
import com.sangeeta.chronomind.repository.OnboardingRepository
import com.sangeeta.chronomind.ui.navigation.MainNavHost
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import javax.inject.Inject
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sangeeta.chronomind.ui.components.TimerFinishedDialog

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingRepository: OnboardingRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        splash.setKeepOnScreenCondition {
            viewModel.uiState.value.isLoading
        }

        enableEdgeToEdge()
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            val finishedTimerActivity by viewModel.finishedTimerActivity
                .collectAsStateWithLifecycle()

            var dismissFinishedTimerDialog by remember {
                mutableStateOf(false)
            }

            androidx.compose.runtime.LaunchedEffect(finishedTimerActivity?.id) {
                if (finishedTimerActivity != null) {
                    dismissFinishedTimerDialog = false
                }
            }

            if (state.isOnboardingComplete) {
                MainNavHost()
            } else {
                OnboardingNavHost(
                    onNavigateToMain = {}
                )
            }
            if (finishedTimerActivity != null && !dismissFinishedTimerDialog) {
                TimerFinishedDialog(
                    onStop = {
                        dismissFinishedTimerDialog = true
                        viewModel.stopFinishedTimer()
                    },
                    onComplete = {
                        dismissFinishedTimerDialog = true
                        viewModel.completeFinishedTimer()
                    }
                )
            }
        }
    }
}
