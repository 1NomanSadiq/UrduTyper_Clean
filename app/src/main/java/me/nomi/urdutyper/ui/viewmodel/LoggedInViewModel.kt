package me.nomi.urdutyper.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import me.nomi.urdutyper.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoggedInViewModel
@Inject
constructor(
    private var firebaseRepository: FirebaseRepositoryImpl,
    dispatchers: DispatchersProviders
) : BaseViewModel(dispatchers) {

    fun logOut() {
        viewModelScope.launch {
            firebaseRepository.logOut()
        }
    }

}