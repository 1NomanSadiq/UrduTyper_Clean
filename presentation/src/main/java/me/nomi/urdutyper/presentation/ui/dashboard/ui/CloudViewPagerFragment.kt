package me.nomi.urdutyper.presentation.ui.dashboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.nomi.urdutyper.databinding.FragmentViewpagerBinding
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.presentation.app.base.BaseFragment
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardNavigationState
import me.nomi.urdutyper.presentation.ui.dashboard.viewmodel.DashboardViewModel
import me.nomi.urdutyper.presentation.ui.dashboard.viewmodel.SharedViewModel
import me.nomi.urdutyper.presentation.utils.common.ImageMaker.getListOfImages
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle

@AndroidEntryPoint
class CloudViewPagerFragment : BaseFragment<FragmentViewpagerBinding>() {
    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: BottomSheetViewPagerAdapter
    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentViewpagerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewPager()
        setCollectors()
    }

    private fun setViewPager() {
        adapter = BottomSheetViewPagerAdapter()
        adapter.apply {
            isCloud = true
            onDelete = {
                viewModel.deleteImage(prefs.uid, it)
            }
        }
        binding.viewPager.apply {
            adapter = this@CloudViewPagerFragment.adapter
        }
    }

    private fun setCollectors() {
        launchAndRepeatWithViewLifecycle {
            launch { viewModel.navigationState.collectLatest { handleNavigationState(it) } }
            launch { sharedViewModel.cloudImageList.collectLatest { loadImages(it) } }
            launch { sharedViewModel.position.collectLatest { binding.viewPager.setCurrentItem(it, false) } }
        }
    }

    private fun handleNavigationState(state: DashboardNavigationState) {
        when (state) {
            is DashboardNavigationState.ImageDeleted -> {
                sharedViewModel.shouldRefresh.value = true
                adapter.notifyItemRemoved(adapter.getItemPosition(state.image))
                if (adapter.isEmpty())
                    findNavController().navigateUp()
            }
            else -> {}
        }
    }

    private fun loadImages(images: List<Image>) {
        adapter.pushData(images)
    }

}