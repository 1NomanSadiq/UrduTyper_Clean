package me.nomi.urdutyper.presentation.ui.onboarding.ui

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.databinding.ActivityOnboardingBinding
import me.nomi.urdutyper.presentation.app.base.BaseActivity

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityOnboardingBinding.inflate(inflater)
}