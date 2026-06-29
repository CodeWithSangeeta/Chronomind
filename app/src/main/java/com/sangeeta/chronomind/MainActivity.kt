package com.sangeeta.chronomind

import android.os.Build
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
import android.Manifest
import androidx.activity.viewModels
import javax.inject.Inject
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

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
        setContent {


            val state by viewModel.uiState.collectAsStateWithLifecycle()

            if (state.isOnboardingComplete) {
                MainNavHost()
            } else {
                OnboardingNavHost(
                    onNavigateToMain = {}
                )
            }
        }
    }
}
