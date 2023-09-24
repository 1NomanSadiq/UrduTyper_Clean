package me.nomi.urdutyper.ui.splash

import android.annotation.SuppressLint
import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.databinding.ActivitySplashScreenBinding
import me.nomi.urdutyper.ui.base.BaseActivity

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater): ActivitySplashScreenBinding =
        ActivitySplashScreenBinding.inflate(inflater)
}