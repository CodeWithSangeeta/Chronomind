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
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingRepository: OnboardingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // In MainActivity, add to onCreate after setContent:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
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
        }
    }
}
