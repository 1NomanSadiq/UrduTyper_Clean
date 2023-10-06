package me.nomi.urdutyper.presentation.ui.dashboard.ui

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
import me.nomi.urdutyper.databinding.FragmentCloudDashboardBinding
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.presentation.app.base.BaseFragment
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardNavigationState
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardUiState
import me.nomi.urdutyper.presentation.ui.dashboard.view.DashboardView
import me.nomi.urdutyper.presentation.ui.dashboard.viewmodel.DashboardViewModel
import me.nomi.urdutyper.presentation.utils.extensions.adapter.attach
import me.nomi.urdutyper.presentation.utils.extensions.common.dialog
import me.nomi.urdutyper.presentation.utils.extensions.common.toast
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle


@AndroidEntryPoint
class CloudDashboardFragment : BaseFragment<FragmentCloudDashboardBinding>(), DashboardView {
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var imageSheet: ImageSheet
    private val adapter by lazy { DashboardAdapter() }

    override fun inflateViewBinding(inflater: LayoutInflater) = FragmentCloudDashboardBinding.inflate(inflater)

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
                viewModel.logout()
            }
            newImage.setOnClickListener {
                createNew()
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
            is DashboardUiState.Error -> showMessageDialog(it.message)
            is DashboardUiState.ShowImages -> showImages(it.images)
            else -> {}
        }
    }

    private fun handleNavigationState(state: DashboardNavigationState) = when (state) {
        is DashboardNavigationState.Logout -> logout()
        is DashboardNavigationState.ShowBottomSheet -> openImage(state.image)
        is DashboardNavigationState.ImageDeleted -> dismissSheet()
    }

    override fun showImages(images: List<Image>) {
        adapter.pushData(images)
    }

    override fun showMessageDialog(message: String) {
        dialog(message).show()
    }

    override fun openImage(image: Image) {
        imageSheet = ImageSheet()
        imageSheet.loadImage = {
            Glide.with(it.root.context)
                .load(image.url)
                .error(R.drawable.not_found)
                .into(it.bigImage)
        }
        imageSheet.fileName = image.fileName
        imageSheet.deleteImage = {
            viewModel.deleteImage(prefs.uid, image.fileName, image.url)
        }
        imageSheet.show(
            requireActivity().supportFragmentManager,
            "openImageSheet"
        )
    }

    private fun dismissSheet() {
        viewModel.loadImageList(prefs.uid)
        imageSheet.dismiss()
        toast("Deleted")
    }

    private fun logout() {
        findNavController().navigate(DashboardFragmentDirections.toOnboardingActivity())
        finishAffinity()
    }

    private fun createNew() =
        findNavController().navigate(DashboardFragmentDirections.toTypeActivity())

    override fun onResume() {
        super.onResume()
        viewModel.loadImageList(prefs.uid)
    }

}