package com.akky.onboarding.presentation.screen

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.akky.onboarding.data.model.EducationCard
import com.akky.onboarding.presentation.utils.toComposeColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.akky.onboarding.MainActivity
import com.akky.onboarding.data.model.ManualBuyEducationData
import com.akky.onboarding.presentation.components.CardsSequenceContent
import com.akky.onboarding.presentation.components.LandingPageContent
import com.akky.onboarding.presentation.components.OnboardingTopBar
import com.akky.onboarding.presentation.utils.toComposeColor
import com.akky.onboarding.presentation.viewmodel.AnimationPhase
import com.akky.onboarding.presentation.viewmodel.CardAnimationState
import com.akky.onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel
) {
    val topBarData by viewModel.topBarData
    val animationPhase by viewModel.animationPhase
    val uiState by viewModel.uiState
    val backgroundCardIndex by viewModel.backgroundCardIndex
    val activity = LocalActivity.current as? MainActivity
    
    val backgroundColor = when {
        uiState.data == null -> Color(0xFF6B46C1)
        backgroundCardIndex < uiState.data!!.educationCardList.size -> {
            uiState.data!!.educationCardList[backgroundCardIndex].backGroundColor.toComposeColor()
        }
        else -> Color(0xFF6B46C1)
    }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.8f),
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.9f)
                    )
                )
            ),
        topBar = {
            OnboardingTopBar(
                title = topBarData?.title ?: "Onboarding",
                iconUrl = topBarData?.iconUrl,
                onBackClick = { 
                    when (animationPhase) {
                        AnimationPhase.LANDING_PAGE -> viewModel.onBackFromLanding()
                        else -> { activity?.finish() }
                    }
                },
                showBackButton = animationPhase != AnimationPhase.SPLASH
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        OnboardingContent(
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun OnboardingContent(
    viewModel: OnboardingViewModel,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState
    val animationPhase by viewModel.animationPhase
    val cardStates by viewModel.cardStates
    val currentExpandedCard by viewModel.currentExpandedCard

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }
            uiState.error != null -> {
                ErrorScreen(error = uiState.error!!)
            }
            uiState.data != null -> {
                DataContent(
                    data = uiState.data!!,
                    animationPhase = animationPhase,
                    cardStates = cardStates,
                    currentExpandedCard = currentExpandedCard,
                    isAnimationComplete = viewModel.isAnimationComplete.value,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun DataContent(
    data: ManualBuyEducationData,
    animationPhase: AnimationPhase,
    cardStates: List<CardAnimationState>,
    currentExpandedCard: Int,
    isAnimationComplete: Boolean,
    viewModel: OnboardingViewModel
) {
    when (animationPhase) {
        AnimationPhase.SPLASH -> {
            SplashScreen(data = data)
        }
        AnimationPhase.CARDS_SEQUENCE -> {
            CardsSequenceContent(
                data = data,
                cardStates = cardStates,
                currentExpandedCard = currentExpandedCard,
                isAnimationComplete = isAnimationComplete,
                onCardClick = viewModel::onCardClicked,
                onSaveClick = { viewModel.onSaveButtonClicked() }
            )
        }
        AnimationPhase.FINAL_CTA -> {
            FinalCTAScreen(
                data = data,
                cardStates = cardStates,
                onSaveClick = { viewModel.onSaveButtonClicked() }
            )
        }
        AnimationPhase.LANDING_PAGE -> {
            LandingPageContent()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorScreen(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Error",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SplashScreen(data: ManualBuyEducationData) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.introSubtitleIcon)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = data.introTitle,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = data.introSubtitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FinalCTAScreen(
    data: ManualBuyEducationData,
    cardStates: List<CardAnimationState>,
    onSaveClick: () -> Unit
) {
    // This would be the final CTA screen implementation
    // For now, just redirect to cards sequence
    CardsSequenceContent(
        data = data,
        cardStates = cardStates,
        currentExpandedCard = cardStates.size - 1,
        isAnimationComplete = true,
        onCardClick = { },
        onSaveClick = onSaveClick
    )
}

@Composable
fun AnimatedEducationCard(
    card: EducationCard,
    cardState: CardAnimationState,
    isCurrentlyExpanded: Boolean,
    isClickable: Boolean = false,
    onCardClick: () -> Unit = {},
    actionText: String = "",
    modifier: Modifier = Modifier
) {
    val animatedOffsetY by animateFloatAsState(
        targetValue = cardState.offsetY,
        animationSpec = tween(durationMillis = 500),
        label = "cardOffset"
    )
    
    val animatedHeight by animateFloatAsState(
        targetValue = if (isClickable) {
            // After animation complete: use click-based expansion
            if (isCurrentlyExpanded) 450f else 80f
        } else {
            // During initial animation: use animation sequence state
            if (cardState.isExpanded) 450f else 80f
        },
        animationSpec = tween(durationMillis = 500),
        label = "cardHeight"
    )
    
    val animatedTiltAngle by animateFloatAsState(
        targetValue = cardState.tiltAngle,
        animationSpec = tween(durationMillis = 500), // Slower animation to make it more visible
        label = "cardTilt"
    )
    
    
    AnimatedVisibility(
        visible = cardState.isVisible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Card(
            modifier = modifier
                .height(animatedHeight.dp)
                .offset(y = animatedOffsetY.dp)
                .graphicsLayer {
                    rotationZ = animatedTiltAngle
                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (cardState.cardIndex % 2 == 0) 0f else 1f,
                        pivotFractionY = 0.5f
                    )
                }
                .clickable(enabled = isClickable) { onCardClick() },
            shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isClickable) {
                        if (isCurrentlyExpanded) Color.Transparent else Color.White.copy(alpha = 0.1f)
                    } else {
                        if (cardState.isExpanded) Color.Transparent else Color.White.copy(alpha = 0.1f)
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isClickable) {
                        if (isCurrentlyExpanded) 8.dp else 2.dp
                    } else {
                        if (cardState.isExpanded) 8.dp else 2.dp
                    }
                )
        ) {
            val animatedImageSize by animateFloatAsState(
                targetValue = if (isClickable) {
                    if (isCurrentlyExpanded) 350f else 50f
                } else {
                    if (cardState.isExpanded) 350f else 50f
                },
                animationSpec = tween(durationMillis = 800),
                label = "imageSize"
            )
            
            val animatedImageCornerRadius by animateFloatAsState(
                targetValue = if (isClickable) {
                    if (isCurrentlyExpanded) 16f else 25f
                } else {
                    if (cardState.isExpanded) 16f else 25f
                },
                animationSpec = tween(durationMillis = 800),
                label = "imageCornerRadius"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                card.startGradient.toComposeColor(),
                                card.endGradient.toComposeColor()
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                val shouldShowExpanded = if (isClickable) {
                    // After animation complete: use click-based expansion
                    isCurrentlyExpanded
                } else {
                    // During initial animation: use animation sequence state
                    cardState.isExpanded
                }
                
                if (shouldShowExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(card.image)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(animatedImageSize.dp)
                                .clip(RoundedCornerShape(animatedImageCornerRadius.dp))
                                .weight(1f, fill = false),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = card.expandStateText,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        
                        if (actionText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = actionText,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(
                                        Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(card.image)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(animatedImageSize.dp)
                                .clip(RoundedCornerShape(animatedImageCornerRadius.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = card.collapsedStateText,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
