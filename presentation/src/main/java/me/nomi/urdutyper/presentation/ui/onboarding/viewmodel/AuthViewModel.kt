package me.nomi.urdutyper.presentation.ui.onboarding.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nomi.urdutyper.domain.usecase.LoginUseCase
import me.nomi.urdutyper.domain.usecase.Register
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.domain.utils.onError
import me.nomi.urdutyper.domain.utils.onSuccess
import me.nomi.urdutyper.presentation.app.base.BaseViewModel
import me.nomi.urdutyper.presentation.ui.onboarding.state.OnboardingNavigationState
import me.nomi.urdutyper.presentation.ui.onboarding.state.OnboardingUiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val login: LoginUseCase,
    private val register: Register,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    private val _viewState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Init)
    val viewState = _viewState.asStateFlow()

    private val _navigationState: MutableSharedFlow<OnboardingNavigationState> = MutableSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    suspend fun login(email: String, password: String) {
        launchOnMainImmediate {
            _viewState.update {
                OnboardingUiState.Loading
            }
            login.invoke(email, password).onSuccess {
                _navigationState.emit(OnboardingNavigationState.GoToDashboard(it))
            }.onError { error ->
                _viewState.update {
                    OnboardingUiState.Error(
                        error.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    suspend fun register(email: String, password: String) {
        launchOnMainImmediate {
            register.invoke(email, password).onSuccess {
                _viewState.update { OnboardingUiState.Registered }
            }.onError { error ->
                _viewState.update {
                    OnboardingUiState.Error(
                        error.message ?: "Something went wrong"
                    )
                }
            }
        }
    }
}
