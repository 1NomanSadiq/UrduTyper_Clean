package me.nomi.urdutyper.presentation.ui.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.usecase.LoadImages
import me.nomi.urdutyper.data.source.SharedPreference
import me.nomi.urdutyper.presentation.base.BaseViewModel
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.domain.utils.onError
import me.nomi.urdutyper.domain.utils.onSuccess
import javax.inject.Inject
@HiltViewModel
class DashboardViewModel @Inject constructor(
    prefs: SharedPreference,
    private val loadImages: LoadImages,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Init)
    val uiState = _uiState.asStateFlow()

    sealed class NavigationState {
        data class LoadImage(val image: Image) : NavigationState()
    }

    private val _navigationState: MutableSharedFlow<NavigationState.LoadImage> =
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

    fun onImageClick(image: Image) = launchOnMainImmediate {
        _navigationState.emit(NavigationState.LoadImage(image))
    }
}