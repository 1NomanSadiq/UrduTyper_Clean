package me.nomi.urdutyper.presentation.ui.dashboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.FragmentDashboardBinding
import me.nomi.urdutyper.presentation.app.base.BaseFragment
import me.nomi.urdutyper.presentation.ui.dashboard.state.DashboardFragmentStateAdapter


@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentDashboardBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = DashboardFragmentStateAdapter(childFragmentManager, lifecycle)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.cloud_dashboard_fragment -> binding.viewPager.currentItem = 0
                R.id.local_dashboard_fragment -> binding.viewPager.currentItem = 1
            }
            true
        }
    }
}