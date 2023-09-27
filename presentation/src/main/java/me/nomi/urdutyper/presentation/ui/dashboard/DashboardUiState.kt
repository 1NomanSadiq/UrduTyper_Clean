package me.nomi.urdutyper.presentation.ui.dashboard

import me.nomi.urdutyper.domain.entity.Image


sealed class DashboardUiState {
    object Init: DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(val images: List<Image>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}