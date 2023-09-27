package me.nomi.urdutyper.presentation.ui.viewmodel

import me.nomi.urdutyper.domain.entity.User


sealed class AuthUiState {
    object Init: AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}