package me.nomi.urdutyper.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.databinding.ActivityDashboardBinding
import me.nomi.urdutyper.ui.base.BaseActivity

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(inflater)

    private val navController by lazy {
        binding.container.getFragment<Fragment>().findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}