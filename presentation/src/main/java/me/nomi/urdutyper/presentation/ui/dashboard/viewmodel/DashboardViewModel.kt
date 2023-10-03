package me.nomi.urdutyper.presentation.ui.dashboard.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.repository.SharedPreferenceRepository
import me.nomi.urdutyper.domain.usecase.LoadImages
import me.nomi.urdutyper.domain.usecase.Logout
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.domain.utils.onError
import me.nomi.urdutyper.domain.utils.onSuccess
import me.nomi.urdutyper.presentation.app.base.BaseViewModel
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardNavigationState
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardUiState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    prefs: SharedPreferenceRepository,
    private val loadImages: LoadImages,
    private val logout: Logout,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Init)
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<DashboardNavigationState> =
        MutableSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    init {
        loadImageList(prefs.uid)
    }


    private fun loadImageList(uid: String) = launchOnMainImmediate {
        _uiState.update { DashboardUiState.Loading }
        loadImages(uid)
            .onSuccess { images ->
                _uiState.update { DashboardUiState.ShowImages(images) }
            }.onError { error ->
                _uiState.update {
                    DashboardUiState.Error(
                        error.message ?: "Failed to load images from the database"
                    )
                }
            }
    }

    fun logout() = launchOnMainImmediate {
        _uiState.update { DashboardUiState.Loading }
        logout.invoke()
            .onSuccess {
                _navigationState.emit(DashboardNavigationState.Logout)
            }.onError { error ->
                _uiState.update {
                    DashboardUiState.Error(
                        error.message ?: "Failed to logout"
                    )
                }
            }
    }

    fun onImageClick(image: Image) = launchOnMainImmediate {
        _navigationState.emit(DashboardNavigationState.ShowBottomSheet(image))
    }
}