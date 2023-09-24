package me.nomi.urdutyper.ui.dashboard

import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.entity.User


sealed class DashboardUiState {
    object Init: DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(val images: List<Image>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}