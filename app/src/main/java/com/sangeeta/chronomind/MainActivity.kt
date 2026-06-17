package com.sangeeta.chronomind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.main.MainNavHost
import com.sangeeta.chronomind.ui.onboarding.OnboardingNavHost
import com.sangeeta.chronomind.repository.OnboardingRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingRepository: OnboardingRepository  // ✅ Hilt injects this

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
                    onNavigateToMain = {
                        // State is driven by DataStore; recompose handles navigation
                    }
                )


            }
        }
    }
}
