package me.nomi.urdutyper.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nomi.urdutyper.databinding.ActivityLoginBinding
import me.nomi.urdutyper.presentation.base.BaseActivity
import me.nomi.urdutyper.presentation.ui.dashboard.DashboardActivity
import me.nomi.urdutyper.presentation.ui.viewmodel.AuthUiState
import me.nomi.urdutyper.presentation.ui.viewmodel.AuthViewModel
import me.nomi.urdutyper.presentation.utils.extensions.common.start
import me.nomi.urdutyper.presentation.utils.extensions.common.toast
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityLoginBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityLoginBinding.inflate(inflater)

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle login button click
        binding.loginButton.setOnClickListener {
            // Get email and password from input fields
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            // Validate input fields
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Login user with email and password
                lifecycleScope.launch { viewModel.login(email, password) }
            } else {
                toast("Missing Field")
            }
        }

        observeViewModel()
    }


    private fun observeViewModel() = with(viewModel) {
        launchAndRepeatWithViewLifecycle {
            launch { viewModel.viewState.collect { handleFeedUiState(it) } }
//            launch { navigationState.collect { handleNavigationState(it) } }
        }
    }

    private fun handleFeedUiState(it: AuthUiState) {
        binding.loginprogressBar.isVisible = it is AuthUiState.Loading
        binding.loginButton.isVisible = it !is AuthUiState.Loading
        when (it) {
            is AuthUiState.Success -> {
                prefs.uid = it.user.id
                start<DashboardActivity>()
                Toast.makeText(applicationContext, "Welcome ${it.user.name}", Toast.LENGTH_LONG)
                    .show()
            }

            is AuthUiState.Error -> {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG)
                    .show()
            }

            else -> {}
        }
    }
//
//    private fun handleNavigationState(state: AuthViewModel.NavigationState) = when (state) {
//        is AuthViewModel.NavigationState.SignIn -> navigateToDash()
//    }


//    private fun navigateToDash() = findNavController(R.id.container).navigate(
//
//    )

//        lifecycle.coroutineScope.launchWhenCreated {
//            authViewModel.user.collect {
//                if (it.isLoading) {
//                    binding.authContainer.progressCircular.visibility = View.VISIBLE
//                }
//                if (it.error.isNotBlank()) {
//                    binding.authContainer.progressCircular.visibility = View.GONE
//                    this@MainActivity.displayToast(it.error)
//                }
//                it.data?.let {
//                    binding.authContainer.progressCircular.visibility = View.GONE
//                    startActivity(Intent(this@MainActivity, DashActivity::class.java))
//                }
//            }
//        }


//        binding.authContainer.btnSignUp.setOnClickListener {
//            with(binding.authContainer) {
//                email = edtEmailID.text.toString()
//                password = edtPassword.text.toString()
//
//                val user = User(
//                    name = "Jahid Hasan",
//                    image = "",
//                    email = email,
//                    active = true,
//                    address = "Dhaka, Bangladesh"
//                )
//
//                if (email.isNotEmpty() && password.isNotEmpty()) {
//                    authViewModel.register(email, password, user)
//                } else {
//                    this@MainActivity.displayToast("Email and Password Must be Entered.")
//                }
//            }
//        }

}