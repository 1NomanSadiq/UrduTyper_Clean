package me.nomi.urdutyper.presentation.ui.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nomi.urdutyper.domain.usecase.UserData
import me.nomi.urdutyper.presentation.base.BaseViewModel
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.domain.utils.onError
import me.nomi.urdutyper.domain.utils.onSuccess
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel
@Inject
constructor(
    private val userData: UserData,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    sealed class NavigationState {
        // Define your navigation states here if needed
        data object LoggedIn : NavigationState()
        data object NotLoggedIn: NavigationState()
    }

    private val _viewState = MutableStateFlow<SplashUiState>(SplashUiState.Init)
    val viewState = _viewState.asStateFlow()

    private val _navigationState: MutableSharedFlow<NavigationState> = MutableSharedFlow()
    val navigationState = _navigationState.asSharedFlow()


    init {
        onInitialState()
    }

    private fun onInitialState() = launchOnMainImmediate {
        delay(2000)
        userData().onSuccess { result ->
            _viewState.update {
                SplashUiState.Success(result)
            }
            _navigationState.emit(NavigationState.LoggedIn)
        }.onError { error ->
            _viewState.update {
                SplashUiState.Error(error.message ?: "Failed to fetch the user data")
            }
        }
    }
}

