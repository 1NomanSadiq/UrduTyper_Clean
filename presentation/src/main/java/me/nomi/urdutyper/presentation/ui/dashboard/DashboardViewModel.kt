package me.nomi.urdutyper.presentation.ui.dashboard

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
import me.nomi.urdutyper.presentation.base.BaseViewModel
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

    sealed class NavigationState {
        data class LoadImage(val image: Image) : NavigationState()
        data object Logout : NavigationState()
    }

    private val _navigationState: MutableSharedFlow<NavigationState> =
        MutableSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    init {
        loadImageList(prefs.uid)
    }


    private fun loadImageList(uid: String) = launchOnMainImmediate {
        _uiState.update { DashboardUiState.Loading }
        loadImages(uid)
            .onSuccess { images ->
                _uiState.update {
                    DashboardUiState.Success(images)
                }
            }.onError { error ->
                _uiState.update {
                    DashboardUiState.Error(
                        error.message ?: "Failed to load images from the database"
                    )
                }
            }
    }

    private fun logout() = launchOnMainImmediate {
        _uiState.update { DashboardUiState.Loading }
        logout.invoke()
            .onSuccess {
                _navigationState.emit(NavigationState.Logout)
            }.onError { error ->
                _uiState.update {
                    DashboardUiState.Error(
                        error.message ?: "Failed to load images from the database"
                    )
                }
            }
    }

    fun onImageClick(image: Image) = launchOnMainImmediate {
        _navigationState.emit(NavigationState.LoadImage(image))
    }
}