package com.akky.onboarding.domain.usecase

import com.akky.onboarding.data.model.ManualBuyEducationData
import com.akky.onboarding.data.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class GetOnboardingDataUseCase(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Flow<Result<ManualBuyEducationData>> = flow {
        try {
            val result = repository.getEducationMetadata()
            val data = result.getOrThrow().data.manualBuyEducationData
            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
