package com.akky.onboarding.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.akky.onboarding.data.model.ManualBuyEducationData
import com.akky.onboarding.presentation.screen.AnimatedEducationCard
import com.akky.onboarding.presentation.utils.toComposeColor
import com.akky.onboarding.presentation.viewmodel.CardAnimationState

@Composable
fun CardsSequenceContent(
    data: ManualBuyEducationData,
    cardStates: List<CardAnimationState>,
    currentExpandedCard: Int,
    isAnimationComplete: Boolean,
    onCardClick: (Int) -> Unit,
    onSaveClick: () -> Unit
) {
    // Content without Scaffold (since parent has it)
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            if (isAnimationComplete) {
                FloatingActionButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .height(48.dp)
                        .wrapContentWidth(),
                    containerColor = data.saveButtonCta.backgroundColor.toComposeColor(),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = data.saveButtonCta.text,
                            color = data.saveButtonCta.textColor.toComposeColor(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val composition by rememberLottieComposition(LottieCompositionSpec.Url(data.ctaLottie))
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        // Cards Container - Scrollable list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding( horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = true
        ) {
            items(data.educationCardList.size) { index ->
                if (index < cardStates.size) {
                    AnimatedEducationCard(
                        card = data.educationCardList[index],
                        cardState = cardStates[index],
                        isCurrentlyExpanded = currentExpandedCard == index,
                        isClickable = isAnimationComplete,
                        onCardClick = { onCardClick(index) },
                        actionText = data.actionText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )
                }
            }
            
            // Add some bottom padding when animation is complete
            if (isAnimationComplete) {
                item {
                    Spacer(modifier = Modifier.height(100.dp)) // Space for floating button
                }
            }
        }
    }
}
