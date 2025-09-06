package com.akky.onboarding.data.repository

import com.akky.onboarding.data.model.OnboardingResponse
import com.akky.onboarding.data.network.OnboardingApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class OnboardingRepository(
    private val apiService: OnboardingApiService
) {
    
    suspend fun getEducationMetadata(): Result<OnboardingResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getEducationMetadata()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
