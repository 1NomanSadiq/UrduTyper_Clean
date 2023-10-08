package me.nomi.urdutyper.presentation.ui.type.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.EditText
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.ActivityTypeBinding
import me.nomi.urdutyper.presentation.app.base.BaseActivity
import me.nomi.urdutyper.presentation.utils.extensions.common.inputMethodManager
import me.nomi.urdutyper.presentation.utils.extensions.views.hideKeyboard

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class TypeActivity : BaseActivity<ActivityTypeBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater) =
        ActivityTypeBinding.inflate(inflater)
}