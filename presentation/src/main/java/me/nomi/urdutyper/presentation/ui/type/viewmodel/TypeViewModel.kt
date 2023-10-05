package me.nomi.urdutyper.presentation.ui.type.viewmodel

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TypeViewModel : ViewModel() {
    private val _gradientOrientation: MutableStateFlow<GradientDrawable.Orientation> =
        MutableStateFlow(GradientDrawable.Orientation.TR_BL)
    private val _font: MutableStateFlow<Typeface?> = MutableStateFlow(null)
    private val _fontNumber = MutableStateFlow(8)
    private val _isBold = MutableStateFlow(false)
    private val _isItalic = MutableStateFlow(false)
    private val _textColor = MutableStateFlow(Color.BLACK)
    private val _leftGradientColor = MutableStateFlow(-0xc0ae4b)
    private val _rightGradientColor = MutableStateFlow(-0xc0ae4b)
    private val _size = MutableStateFlow(50f)

    val gradientOrientation: Flow<GradientDrawable.Orientation> = _gradientOrientation
    val font: Flow<Typeface?> = _font
    val fontNumber: Flow<Int> = _fontNumber
    val isBold: Flow<Boolean> = _isBold
    val isItalic: Flow<Boolean> = _isItalic
    val textColor: Flow<Int> = _textColor
    val leftGradientColor: Flow<Int> = _leftGradientColor
    val rightGradientColor: Flow<Int> = _rightGradientColor
    val size: Flow<Float> = _size

    val currentGradientOrientation get() = _gradientOrientation.value
    val currentFont  get() = _font.value
    val currentFontNumber get() = _fontNumber.value
    val isCurrentlyBold get() = _isBold.value
    val isCurrentlyItalic get() = _isItalic.value
    val currentTextColor get() = _textColor.value
    val currentLeftGradientColor get() = _leftGradientColor.value
    val currentRightGradientColor get() = _rightGradientColor.value
    val currentSize get() = _size.value

    fun setGradientOrientation(orientation: GradientDrawable.Orientation) {
        _gradientOrientation.value = orientation
    }

    fun setFont(font: Typeface?) {
        _font.value = font
    }

    fun setFontNumber(number: Int) {
        _fontNumber.value = number
    }

    fun setBold(bold: Boolean) {
        _isBold.value = bold
    }

    fun setItalic(italic: Boolean) {
        _isItalic.value = italic
    }

    fun setTextColor(color: Int) {
        _textColor.value = color
    }

    fun setLeftGradientColor(color: Int) {
        _leftGradientColor.value = color
    }

    fun setSolidColor(color: Int) {
        _leftGradientColor.value = color
        _rightGradientColor.value = _leftGradientColor.value
    }

    fun setRightGradientColor(color: Int) {
        _rightGradientColor.value = color
    }

    fun setSize(size: Float) {
        _size.value = size
    }
}
