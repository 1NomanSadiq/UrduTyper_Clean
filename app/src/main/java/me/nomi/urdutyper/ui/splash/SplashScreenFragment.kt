package me.nomi.urdutyper.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nomi.urdutyper.databinding.FragmentSplashScreenBinding
import me.nomi.urdutyper.ui.base.BaseFragment
import me.nomi.urdutyper.utils.extensions.common.dialog
import me.nomi.urdutyper.utils.extensions.views.launchAndRepeatWithViewLifecycle

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
    }


    private fun observeViewModel() = with(viewModel) {
        launchAndRepeatWithViewLifecycle {
            launch { viewState.collect { handleUiState(it) } }
            launch { navigationState.collect { handleNavigationState(it) } }
        }

    }

    private fun handleUiState(it: SplashUiState) {
        when (it) {
            is SplashUiState.Success -> {}

            is SplashUiState.Error -> {
                dialog(it.message).show()
            }

            else -> {}
        }
    }

    private fun handleNavigationState(state: SplashScreenViewModel.NavigationState) = when (state) {
        is SplashScreenViewModel.NavigationState.LoggedIn -> navigateToDashboard()
        is SplashScreenViewModel.NavigationState.NotLoggedIn -> navigateToMain()
    }


    private fun navigateToDashboard() = findNavController().navigate(
        SplashScreenFragmentDirections.toDashboard()
    )

    private fun navigateToMain() = findNavController().navigate(
        SplashScreenFragmentDirections.toMainActivity()
    )
}