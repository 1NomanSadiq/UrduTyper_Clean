package me.nomi.urdutyper.presentation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.FragmentDashboardBinding
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.presentation.base.BaseFragment
import me.nomi.urdutyper.presentation.utils.extensions.adapter.attach
import me.nomi.urdutyper.presentation.utils.extensions.common.dialog
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle


@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(), DashboardView {
    private val viewModel: DashboardViewModel by viewModels()
    private val adapter by lazy { DashboardAdapter() }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentDashboardBinding =
        FragmentDashboardBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        setupRecyclerView()
        setClickListeners()
    }

    private fun setupRecyclerView() {
        binding.recView.attach(
            layoutManager = GridLayoutManager(requireActivity(), 3),
            adapter = adapter,
            onItemClick = { _, item ->
                viewModel.onImageClick(item)
            }
        )
    }

    private fun setClickListeners() {
        binding.apply {
            logOut.setOnClickListener {
                logout()
            }
        }
    }

    private fun observeViewModel() = with(viewModel) {
        launchAndRepeatWithViewLifecycle {
            launch { uiState.collect { handleUiState(it) } }
            launch { navigationState.collect { handleNavigationState(it) } }
        }
    }

    private fun handleUiState(it: DashboardUiState) {
        when (it) {
            is DashboardUiState.Error -> {
                showMessageDialog(it.message)
            }

            is DashboardUiState.Success -> {
                showImages(it.images)
            }

            else -> {}
        }
    }

    private fun handleNavigationState(state: DashboardViewModel.NavigationState) = when (state) {
        is DashboardViewModel.NavigationState.LoadImage -> navigateToImage(state.image)
        is DashboardViewModel.NavigationState.Logout -> logout()
    }

    override fun showImages(images: List<Image>) {
        adapter.pushData(images)
    }

    override fun showMessageDialog(message: String) {
        dialog(message).show()
    }

    override fun navigateToImage(image: Image) {
        val imageSheet = ImageSheet()
        imageSheet.loadImage = {
            Glide.with(it.root.context)
                .load(image.url)
                .placeholder(R.drawable.not_found)
                .into(it.bigImage)
        }
        imageSheet.show(
            requireActivity().supportFragmentManager,
            "openImageSheet"
        )
    }

    override fun logout() {
        findNavController().navigate(DashboardFragmentDirections.toMainActivity())
        finishActivity()
    }

}