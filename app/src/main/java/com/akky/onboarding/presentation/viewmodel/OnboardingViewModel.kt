package com.akky.onboarding.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akky.onboarding.data.model.ManualBuyEducationData
import com.akky.onboarding.domain.usecase.CardAnimationUseCase
import com.akky.onboarding.domain.usecase.GetOnboardingDataUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val cardAnimationUseCase: CardAnimationUseCase
) : ViewModel() {
    
    private val _uiState = mutableStateOf(OnboardingUiState())
    val uiState: State<OnboardingUiState> = _uiState
    
    private val _animationPhase = mutableStateOf(AnimationPhase.SPLASH)
    val animationPhase: State<AnimationPhase> = _animationPhase
    
    private val _cardStates = mutableStateOf<List<CardAnimationState>>(emptyList())
    val cardStates: State<List<CardAnimationState>> = _cardStates
    
    private val _currentExpandedCard = mutableStateOf(-1)
    val currentExpandedCard: State<Int> = _currentExpandedCard
    
    private val _backgroundCardIndex = mutableStateOf(0)
    val backgroundCardIndex: State<Int> = _backgroundCardIndex
    
    private val _isAnimationComplete = mutableStateOf(false)
    val isAnimationComplete: State<Boolean> = _isAnimationComplete
    
    private val _topBarData = mutableStateOf<TopBarData?>(null)
    val topBarData: State<TopBarData?> = _topBarData
    
    init {
        loadOnboardingData()
    }
    
    private fun loadOnboardingData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getOnboardingDataUseCase().collect { result ->
                result.fold(
                    onSuccess = { data ->
                        _uiState.value = OnboardingUiState(
                            data = data,
                            isLoading = false,
                            error = null
                        )
                        
                        _topBarData.value = TopBarData(data.toolBarText, data.toolBarIcon)
                        
                        // Start the onboarding flow directly (already in viewModelScope)
                        startOnboardingFlow(data)
                    },
                    onFailure = { error ->
                        _uiState.value = OnboardingUiState(
                            data = null,
                            isLoading = false,
                            error = error.message ?: "Unknown error occurred"
                        )
                    }
                )
            }
        }
    }
    
    private suspend fun startOnboardingFlow(data: ManualBuyEducationData) {
        // TEMP FIX: Ignore shouldShowBeforeNavigating flag to show animation sequence
        // if (data.shouldShowBeforeNavigating) {
        //     _animationPhase.value = AnimationPhase.LANDING_PAGE
        //     return
        // }
        
        kotlinx.coroutines.delay(data.collapseExpandIntroInterval.toLong())
        
        _animationPhase.value = AnimationPhase.CARDS_SEQUENCE
        initializeCardStates(data.educationCardList.size)
        
        // Sequential animation - each card waits for the previous one to complete
        data.educationCardList.forEachIndexed { index, _ ->
            val isLastCard = index == data.educationCardList.size - 1
            
            _backgroundCardIndex.value = index
            _currentExpandedCard.value = index
            
            // Wait for each card animation to complete before starting the next
            cardAnimationUseCase.animateCardSequence(
                cardIndex = index,
                data = data,
                isLastCard = isLastCard
            ) { newState ->
                updateCardState(index, newState)
            }
            
            if (!isLastCard) {
                _currentExpandedCard.value = -1
            }
        }
        
        // After all cards have animated, set the final state
        val lastCardIndex = data.educationCardList.size - 1
        _currentExpandedCard.value = lastCardIndex
        _backgroundCardIndex.value = lastCardIndex
        _isAnimationComplete.value = true
    }
    
    private fun initializeCardStates(cardCount: Int) {
        val initialStates = (0 until cardCount).map { index ->
            CardAnimationState(
                cardIndex = index,
                isExpanded = false,
                offsetY = 0f,
                isVisible = false,
                stackPosition = index,
                tiltAngle = 0f
            )
        }
        _cardStates.value = initialStates
    }
    
    private fun updateCardState(cardIndex: Int, newState: CardAnimationState) {
        val currentStates = _cardStates.value.toMutableList()
        if (cardIndex < currentStates.size) {
            currentStates[cardIndex] = newState
            _cardStates.value = currentStates
        }
    }
    
    fun onCardClicked(cardIndex: Int) {
        if (!_isAnimationComplete.value) return
        
        val currentExpanded = _currentExpandedCard.value
        if (currentExpanded == cardIndex) return
        
        _currentExpandedCard.value = cardIndex
        _backgroundCardIndex.value = cardIndex
    }
    
    fun onSaveButtonClicked() {
        _animationPhase.value = AnimationPhase.LANDING_PAGE
    }
    
    fun onBackFromLanding() {
        _animationPhase.value = AnimationPhase.CARDS_SEQUENCE
    }
}

data class OnboardingUiState(
    val data: ManualBuyEducationData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class AnimationPhase {
    SPLASH,
    CARDS_SEQUENCE,
    FINAL_CTA,
    LANDING_PAGE
}

data class CardAnimationState(
    val cardIndex: Int,
    val isExpanded: Boolean,
    val offsetY: Float,
    val isVisible: Boolean,
    val stackPosition: Int,
    val tiltAngle: Float
)

data class TopBarData(
    val title: String,
    val iconUrl: String?
)