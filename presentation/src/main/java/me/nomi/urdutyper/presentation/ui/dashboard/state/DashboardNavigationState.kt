package me.nomi.urdutyper.presentation.ui.dashboard.state

import me.nomi.urdutyper.domain.entity.Image

sealed class DashboardNavigationState {
    data class ShowBottomSheet(val image: Image) : DashboardNavigationState()
    data object ImageDeleted: DashboardNavigationState()
    data object Logout : DashboardNavigationState()
}