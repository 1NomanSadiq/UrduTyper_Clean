package me.nomi.urdutyper.presentation.ui.onboarding.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nomi.urdutyper.databinding.FragmentLoginBinding
import me.nomi.urdutyper.presentation.app.base.BaseFragment
import me.nomi.urdutyper.presentation.ui.onboarding.state.OnboardingNavigationState
import me.nomi.urdutyper.presentation.ui.onboarding.state.OnboardingUiState
import me.nomi.urdutyper.presentation.ui.onboarding.viewmodel.AuthViewModel
import me.nomi.urdutyper.presentation.utils.extensions.common.dialog
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentLoginBinding.inflate(inflater)

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            when {
                email.isEmpty() || password.isEmpty() ->
                    dialog("Please enter both email and password").show()

                !Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() -> dialog("Invalid Email Address").show()

                password.length < 10 -> dialog("Password must at least be 10 characters in length").show()
                else -> lifecycleScope.launch { viewModel.login(email, password) }
            }
        }

        observeViewModel()

    }

    private fun observeViewModel() = with(viewModel) {
        launchAndRepeatWithViewLifecycle {
            launch { viewModel.viewState.collect { handleUiState(it) } }
            launch { navigationState.collect { handleNavigationState(it) } }
        }
    }

    private fun handleUiState(it: OnboardingUiState) {
        binding.loginprogressBar.isVisible = it is OnboardingUiState.Loading
        binding.loginButton.isVisible = it !is OnboardingUiState.Loading
        when (it) {
            is OnboardingUiState.Error -> {
                dialog(it.message).show()
            }

            else -> {}
        }
    }

    private fun handleNavigationState(state: OnboardingNavigationState) = when (state) {
        is OnboardingNavigationState.GoToSignUp -> {
            navigateToSignUp()
        }

        is OnboardingNavigationState.GoToDashboard -> {
            prefs.uid = state.user.id
            navigateToDashboard()
        }
    }


    private fun navigateToSignUp() =
        findNavController().navigate(LoginFragmentDirections.toSignUpFragment())

    private fun navigateToDashboard() {
        findNavController().navigate(LoginFragmentDirections.toDashboardActivity())
        finishAffinity()
    }
}