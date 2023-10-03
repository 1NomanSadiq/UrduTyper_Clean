package me.nomi.urdutyper.presentation.ui.onboarding.state


sealed class OnboardingUiState {
    data object Init : OnboardingUiState()
    data object Loading : OnboardingUiState()
    data object Registered : OnboardingUiState()
    data class Error(val message: String) : OnboardingUiState()
}