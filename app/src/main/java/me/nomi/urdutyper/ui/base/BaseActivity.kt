package me.nomi.urdutyper.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import me.nomi.urdutyper.data.source.SharedPreference
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreference

    protected val binding: VB by lazy { inflateViewBinding(layoutInflater) }

    protected abstract fun inflateViewBinding(inflater: LayoutInflater): VB

//    fun isDarkModeEnabled() = prefs.getBoolean(DARK_MODE, false)

//    fun enableDarkMode(enable: Boolean) = appSettings.edit().putBoolean(DARK_MODE, enable).commit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(if (isDarkModeEnabled()) R.style.DarkTheme else R.style.LightTheme)
        setContentView(binding.root)
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<in T>) =
        observe(this@BaseActivity, observer)

    companion object {
        const val DARK_MODE = "dark_mode"
    }
}