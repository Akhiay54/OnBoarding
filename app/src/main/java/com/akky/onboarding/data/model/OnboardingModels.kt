package com.akky.onboarding.data.model

import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: OnboardingData
)

data class OnboardingData(
    @SerializedName("manualBuyEducationData")
    val manualBuyEducationData: ManualBuyEducationData
)

data class ManualBuyEducationData(
    @SerializedName("toolBarText")
    val toolBarText: String,
    @SerializedName("introTitle")
    val introTitle: String,
    @SerializedName("introSubtitle")
    val introSubtitle: String,
    @SerializedName("educationCardList")
    val educationCardList: List<EducationCard>,
    @SerializedName("saveButtonCta")
    val saveButtonCta: SaveButtonCta,
    @SerializedName("ctaLottie")
    val ctaLottie: String,
    @SerializedName("screenType")
    val screenType: String,
    @SerializedName("cohort")
    val cohort: String,
    @SerializedName("combination")
    val combination: String?,
    @SerializedName("collapseCardTiltInterval")
    val collapseCardTiltInterval: Int,
    @SerializedName("collapseExpandIntroInterval")
    val collapseExpandIntroInterval: Int,
    @SerializedName("bottomToCenterTranslationInterval")
    val bottomToCenterTranslationInterval: Int,
    @SerializedName("expandCardStayInterval")
    val expandCardStayInterval: Int,
    @SerializedName("seenCount")
    val seenCount: Int?,
    @SerializedName("actionText")
    val actionText: String,
    @SerializedName("shouldShowOnLandingPage")
    val shouldShowOnLandingPage: Boolean,
    @SerializedName("toolBarIcon")
    val toolBarIcon: String,
    @SerializedName("introSubtitleIcon")
    val introSubtitleIcon: String,
    @SerializedName("shouldShowBeforeNavigating")
    val shouldShowBeforeNavigating: Boolean
)

data class EducationCard(
    @SerializedName("image")
    val image: String,
    @SerializedName("collapsedStateText")
    val collapsedStateText: String,
    @SerializedName("expandStateText")
    val expandStateText: String,
    @SerializedName("backGroundColor")
    val backGroundColor: String,
    @SerializedName("strokeStartColor")
    val strokeStartColor: String,
    @SerializedName("strokeEndColor")
    val strokeEndColor: String,
    @SerializedName("startGradient")
    val startGradient: String,
    @SerializedName("endGradient")
    val endGradient: String
)

data class SaveButtonCta(
    @SerializedName("text")
    val text: String,
    @SerializedName("deeplink")
    val deeplink: String?,
    @SerializedName("backgroundColor")
    val backgroundColor: String,
    @SerializedName("textColor")
    val textColor: String,
    @SerializedName("strokeColor")
    val strokeColor: String,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("order")
    val order: String?
)