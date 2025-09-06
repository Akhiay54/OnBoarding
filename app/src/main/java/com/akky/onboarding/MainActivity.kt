package com.akky.onboarding

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.akky.onboarding.data.network.OnboardingApiService
import com.akky.onboarding.data.repository.OnboardingRepository
import com.akky.onboarding.domain.usecase.CardAnimationUseCase
import com.akky.onboarding.domain.usecase.GetOnboardingDataUseCase
import com.akky.onboarding.presentation.screen.OnboardingScreen
import com.akky.onboarding.presentation.viewmodel.OnboardingViewModel
import com.akky.onboarding.ui.theme.OnBoardingTheme
class MainActivity : ComponentActivity() {
    // Temporarily create ViewModel manually without Hilt
    private val viewModel: OnboardingViewModel by lazy {
        OnboardingViewModel(
            GetOnboardingDataUseCase(OnboardingRepository(OnboardingApiService.create())),
            CardAnimationUseCase()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configure system bars for SDK 35 (Android 15)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        
        // Ensure status bar is visible on Android 15
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        
        setContent {
            OnBoardingTheme {
                OnboardingScreen(viewModel = viewModel)
            }
        }
    }
}
