package me.nomi.urdutyper.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.nomi.urdutyper.domain.repository.FirebaseRepository
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoggedInViewModel
@Inject
constructor(
    private var firebaseRepository: FirebaseRepository,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    fun logOut() {
        viewModelScope.launch {
            firebaseRepository.logOut()
        }
    }

}