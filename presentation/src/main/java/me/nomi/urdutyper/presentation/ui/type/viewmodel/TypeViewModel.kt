package me.nomi.urdutyper.presentation.ui.type.viewmodel

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TypeViewModel : ViewModel() {
    val gradientOrientation: MutableStateFlow<GradientDrawable.Orientation> =
        MutableStateFlow(GradientDrawable.Orientation.TR_BL)
    val font: MutableStateFlow<Typeface?> = MutableStateFlow(null)
    val fontNumber = MutableStateFlow(8)
    val isBold = MutableStateFlow(false)
    val isItalic = MutableStateFlow(false)
    val textColor = MutableStateFlow(Color.BLACK)
    val leftGradientColor = MutableStateFlow(-0xc0ae4b)
    val rightGradientColor = MutableStateFlow(-0xc0ae4b)
    val size = MutableStateFlow(50f)

    fun setGradientOrientation(orientation: GradientDrawable.Orientation) {
        gradientOrientation.value = orientation
    }

    fun setFont(font: Typeface?) {
        this.font.value = font
    }

    fun setFontNumber(number: Int) {
        fontNumber.value = number
    }

    fun setBold(bold: Boolean) {
        isBold.value = bold
    }

    fun setItalic(italic: Boolean) {
        isItalic.value = italic
    }

    fun setTextColor(color: Int) {
        textColor.value = color
    }

    fun setLeftGradientColor(color: Int) {
        leftGradientColor.value = color
    }

    fun setSolidColor(color: Int) {
        leftGradientColor.value = color
        rightGradientColor.value = leftGradientColor.value
    }

    fun setRightGradientColor(color: Int) {
        rightGradientColor.value = color
    }

    fun setSize(size: Float) {
        this.size.value = size
    }
}
