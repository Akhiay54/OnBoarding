package com.akky.onboarding.domain.usecase

import com.akky.onboarding.data.model.ManualBuyEducationData
import com.akky.onboarding.presentation.viewmodel.CardAnimationState
import kotlinx.coroutines.delay

class CardAnimationUseCase {
    
    suspend fun animateCardSequence(
        cardIndex: Int,
        data: ManualBuyEducationData,
        isLastCard: Boolean,
        onUpdateState: (CardAnimationState) -> Unit
    ) {
        // Step 1: Card appears from bottom
        onUpdateState(
            CardAnimationState(
                cardIndex = cardIndex,
                isVisible = true,
                isExpanded = true,
                offsetY = 1000f, // Start from bottom
                stackPosition = cardIndex,
                tiltAngle = 0f
            )
        )
        delay(200)
        
        // Step 2: Card slides up to center
        onUpdateState(
            CardAnimationState(
                cardIndex = cardIndex,
                isVisible = true,
                isExpanded = true,
                offsetY = 0f, // Move to center
                stackPosition = cardIndex,
                tiltAngle = 0f
            )
        )
        delay(data.expandCardStayInterval.toLong())
        
        if (!isLastCard) {
            // Step 3: Card collapses WHILE tilting (simultaneous animation)
            val tiltAngle = if (cardIndex % 2 == 0) 10f else -10f
            onUpdateState(
                CardAnimationState(
                    cardIndex = cardIndex,
                    isVisible = true,
                    isExpanded = false, // Collapsing
                    offsetY = 0f,
                    stackPosition = cardIndex,
                    tiltAngle = tiltAngle // Tilting at the same time
                )
            )
            delay(1200) // Time for collapse + tilt animation to complete
            
            // Step 4: Adjust to correct position (remove tilt)
            onUpdateState(
                CardAnimationState(
                    cardIndex = cardIndex,
                    isVisible = true,
                    isExpanded = false,
                    offsetY = 0f,
                    stackPosition = cardIndex,
                    tiltAngle = 0f // Straighten the card
                )
            )
            delay(500) // Brief pause before next card
        } else {
            delay(2500) // Total delay for last card
        }
    }
}
