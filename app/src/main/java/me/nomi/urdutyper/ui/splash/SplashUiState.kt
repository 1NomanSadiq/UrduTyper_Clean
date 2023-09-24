package me.nomi.urdutyper.ui.splash

import me.nomi.urdutyper.domain.entity.User


sealed class SplashUiState {
    object Init: SplashUiState()
    object Loading : SplashUiState()
    data class Success(val user: User) : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}