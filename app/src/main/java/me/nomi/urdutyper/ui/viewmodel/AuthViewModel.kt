package me.nomi.urdutyper.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nomi.urdutyper.data.mapper.Mapper.toDomain
import me.nomi.urdutyper.data.source.SharedPreference
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.domain.usecase.LoginUseCase
import me.nomi.urdutyper.domain.usecase.Register
import me.nomi.urdutyper.ui.base.BaseViewModel
import me.nomi.urdutyper.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.utils.extensions.onError
import me.nomi.urdutyper.utils.extensions.onSuccess
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val login: LoginUseCase,
    private val register: Register,
    private val prefs: SharedPreference,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    init {
//        onInitialState()
    }

    fun onInitialState() = launchOnMainImmediate {


    }


    sealed class NavigationState {
        // Define your navigation states here if needed
        data class SignIn(val user: User) : NavigationState()
    }

    private val _viewState = MutableStateFlow<AuthUiState>(AuthUiState.Init)
    val viewState = _viewState.asStateFlow()

    private val _navigationState: MutableSharedFlow<NavigationState> = MutableSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    suspend fun login(email: String, password: String) {
        launchOnMainImmediate {
            _viewState.update {
                AuthUiState.Loading
            }
            login.invoke(email, password).onSuccess { result ->
                _viewState.update {
                    AuthUiState.Success(result.toDomain())
                }
            }.onError { error ->
                _viewState.update { AuthUiState.Error(error.message ?: "") }
            }
        }
    }

    suspend fun register(email: String, password: String, user: User) {
        launchOnMainImmediate {
            register.invoke(email, password, user).onSuccess { result ->
                _viewState.update { AuthUiState.Success(result.toDomain()) }
            }.onError { error ->
                _viewState.update { AuthUiState.Error(error.message ?: "") }
            }
        }
    }
}
