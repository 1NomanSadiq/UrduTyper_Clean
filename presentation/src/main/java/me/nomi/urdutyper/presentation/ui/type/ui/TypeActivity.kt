package me.nomi.urdutyper.presentation.ui.type.ui

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.ActivityOnlineBinding
import me.nomi.urdutyper.databinding.BackgroundColorDialogBinding
import me.nomi.urdutyper.databinding.ColorPickDialogBinding
import me.nomi.urdutyper.databinding.FontStyleDialogBinding
import me.nomi.urdutyper.presentation.ui.type.viewmodel.TypeViewModel
import me.nomi.urdutyper.presentation.utils.common.ImageMaker.pictureFile
import me.nomi.urdutyper.presentation.utils.common.ImageMaker.saveBitMap
import me.nomi.urdutyper.presentation.utils.common.MultiTouchListener
import me.nomi.urdutyper.presentation.utils.common.Notification.create
import me.nomi.urdutyper.presentation.utils.extensions.views.launchAndRepeatWithViewLifecycle
import me.nomi.urdutyper.presentation.utils.permissions.PermissionHandler
import me.nomi.urdutyper.presentation.utils.permissions.Permissions.check
import java.util.Random

@AndroidEntryPoint
class TypeActivity : AppCompatActivity() {
    private val binding: ActivityOnlineBinding by lazy {
        ActivityOnlineBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: TypeViewModel by viewModels()
    private var fonts: ArrayList<Typeface?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.edittext2.setOnTouchListener(MultiTouchListener(this))

        launchAndRepeatWithViewLifecycle {
            launch { viewModel.gradientOrientation.collectLatest { setGradientBackground() } }
            launch { viewModel.leftGradientColor.collectLatest { setGradientBackground() } }
            launch { viewModel.rightGradientColor.collectLatest { setGradientBackground() } }
            launch { viewModel.fontNumber.collectLatest { binding.edittext2.typeface = fonts[it] } }
            launch { viewModel.size.collectLatest { binding.edittext2.textSize = it } }
        }

        fonts = arrayListOf(
            ResourcesCompat.getFont(this, R.font.a_300_regular),
            ResourcesCompat.getFont(this, R.font.aa_sameer_almas_regular),
            ResourcesCompat.getFont(this, R.font.aa_sameer_divangiry_regular),
            ResourcesCompat.getFont(this, R.font.aa_sameer_qamri_regular),
            ResourcesCompat.getFont(this, R.font.aa_sameer_rafiya_unicode_regular),
            ResourcesCompat.getFont(this, R.font.aa_sameer_zikran_regular),
            ResourcesCompat.getFont(this, R.font.aadil_aadil),
            ResourcesCompat.getFont(this, R.font.gandhara_suls_regular),
            ResourcesCompat.getFont(this, R.font.jameel_noori_nastaleeq),
            ResourcesCompat.getFont(this, R.font.jameel_noori_nastaleeq_kasheeda),
            ResourcesCompat.getFont(this, R.font.lobster_regular),
            ResourcesCompat.getFont(this, R.font.alqalam_khat_e_sumbali_regular),
            ResourcesCompat.getFont(this, R.font.alqalam_makki_regular)
        )
        val random = Random()

        binding.root2.setOnClickListener {
            binding.menu2.collapse()
            binding.edittext2.clearFocus()
        }
        binding.edittext2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.menu2.collapse()
        }
        binding.edittext2.setOnClickListener { binding.menu2.collapse() }
        binding.edittext2.setOnEditorActionListener { _, actionId: Int, _ -> actionId == EditorInfo.IME_ACTION_DONE }
        binding.edittext2.setOnFocusChangeListener { v, hasFocus ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                imm.showSoftInputFromInputMethod(v.windowToken, 0)
            } else imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        binding.saveImage2.setOnClickListener {
            checkPermission(object : PermissionHandler {
                override fun onGranted() {
                    binding.menu2.collapse()
                    binding.menu2.visibility = View.GONE
                    binding.edittext2.isCursorVisible = false
                    binding.edittext2.clearFocus()
                    val uri = saveAndUploadFileOnline()
                    create(this@TypeActivity, pictureFile!!.name, uri)
                    binding.menu2.visibility = View.VISIBLE
                }

                override fun onDenied() {
                    Toast.makeText(
                        this@TypeActivity,
                        "Required Permissions are denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        binding.backgroundColor2.setOnClickListener {
            binding.menu2.collapse()
            createDialog(R.layout.background_color_dialog) { dialogBinding, _ ->
                (dialogBinding as BackgroundColorDialogBinding).apply {
                    orientation.setOnClickListener {
                        viewModel.setGradientOrientation(
                            when (viewModel.currentGradientOrientation) {
                                GradientDrawable.Orientation.TR_BL -> GradientDrawable.Orientation.TOP_BOTTOM
                                GradientDrawable.Orientation.TOP_BOTTOM -> GradientDrawable.Orientation.TL_BR
                                GradientDrawable.Orientation.TL_BR -> GradientDrawable.Orientation.LEFT_RIGHT
                                else -> GradientDrawable.Orientation.TR_BL
                            }
                        )
                    }
                    autoBackgroundColor.setOnClickListener { _ ->
                        if (solid.isChecked) {
                            viewModel.setSolidColor(
                                Color.rgb(
                                    random.nextInt(),
                                    random.nextInt(),
                                    random.nextInt()
                                )
                            )
                        } else {
                            viewModel.setLeftGradientColor(
                                Color.rgb(
                                    random.nextInt(),
                                    random.nextInt(),
                                    random.nextInt()
                                )
                            )
                            viewModel.setRightGradientColor(
                                Color.rgb(
                                    random.nextInt(),
                                    random.nextInt(),
                                    random.nextInt()
                                )
                            )
                        }
                    }

                    gradient.setOnCheckedChangeListener { _, isChecked ->
                        orientation.isVisible = isChecked
                        leftGradient.isVisible = isChecked
                        rightGradient.isVisible = isChecked
                        solidBackgroundColor.isVisible = !isChecked
                    }


                    leftGradient.setOnClickListener {
                        showGradientBackgroundColorPickDialog(true)
                    }
                    rightGradient.setOnClickListener {
                        showGradientBackgroundColorPickDialog(false)
                    }

                    solidBackgroundColor.setOnClickListener { showSolidBackgroundColorPickDialog() }
                }
            }.show()
        }

        binding.textStyle2.setOnClickListener {
            binding.menu2.collapse()
            createDialog(R.layout.font_style_dialog) { dialogBinding, _ ->
                (dialogBinding as FontStyleDialogBinding).apply {

                    bold.setOnCheckedChangeListener { _, isChecked ->
                        val typeface = when {
                            isChecked && italic.isChecked -> Typeface.BOLD_ITALIC
                            isChecked -> Typeface.BOLD
                            italic.isChecked -> Typeface.ITALIC
                            else -> {
                                binding.edittext2.setTypeface(
                                    null,
                                    Typeface.NORMAL)
                                var fontNumber = if (viewModel.currentFontNumber == fonts.size - 1) 0
                                else viewModel.currentFontNumber + 1
                                viewModel.setFontNumber(fontNumber)
                                fontNumber = if (viewModel.currentFontNumber == 0) fonts.size -1
                                else viewModel.currentFontNumber - 1
                                viewModel.setFontNumber(fontNumber)
                                Typeface.NORMAL
                            }
                        }
                        binding.edittext2.setTypeface(binding.edittext2.typeface, typeface)
                    }

                    italic.setOnCheckedChangeListener { _, isChecked ->
                        val typeface = when {
                            isChecked && bold.isChecked -> Typeface.BOLD_ITALIC
                            isChecked -> Typeface.ITALIC
                            bold.isChecked -> Typeface.BOLD
                            else -> {
                                binding.edittext2.setTypeface(
                                    null,
                                    Typeface.NORMAL)
                                var fontNumber = if (viewModel.currentFontNumber == fonts.size - 1) 0
                                else viewModel.currentFontNumber + 1
                                viewModel.setFontNumber(fontNumber)
                                fontNumber = if (viewModel.currentFontNumber == 0) fonts.size -1
                                else viewModel.currentFontNumber - 1
                                viewModel.setFontNumber(fontNumber)
                                Typeface.NORMAL
                            }
                        }
                        binding.edittext2.setTypeface(binding.edittext2.typeface, typeface)
                    }

                    fontSize.progress = viewModel.currentSize.toInt() - 20
                    alignLeft.setOnClickListener {
                        binding.edittext2.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                        binding.edittext2.textSize = viewModel.currentSize + 1
                        binding.edittext2.textSize = viewModel.currentSize - 1
                    }
                    alignRight.setOnClickListener {
                        binding.edittext2.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                        binding.edittext2.textSize = viewModel.currentSize + 1
                        binding.edittext2.textSize = viewModel.currentSize - 1
                    }
                    alignCenter.setOnClickListener {
                        binding.edittext2.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        binding.edittext2.textSize = viewModel.currentSize + 1
                        binding.edittext2.textSize = viewModel.currentSize - 1
                    }
                    autoTextColor.setOnClickListener {
                        val color =
                            Color.rgb(random.nextInt(), random.nextInt(), random.nextInt())
                        binding.edittext2.setTextColor(color)
                        binding.edittext2.setHintTextColor(
                            Color.argb(
                                50,
                                Color.red(color),
                                Color.green(color),
                                Color.blue(color)
                            )
                        )
                    }

                    textColor.setOnClickListener { showTextColorPickDialog() }

                    myFont.setOnClickListener {
                        val fontNumber = if (viewModel.currentFontNumber == fonts.size - 1) 0
                        else viewModel.currentFontNumber + 1
                        viewModel.setFontNumber(fontNumber)

                    }

                    fontSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            viewModel.setSize((progress + 20).toFloat())
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                }
            }.show()
        }
    }

    private fun showColorPickDialog(
        initialColor: Int,
        onColorSelected: (Int) -> Unit
    ) {
        createDialog(R.layout.color_pick_dialog) { dialogBinding, dialog ->
            (dialogBinding as ColorPickDialogBinding).apply {
                picker.showOldCenterColor = false
                picker.addSVBar(svbar)
                picker.setNewCenterColor(initialColor)
                picker.color = if (initialColor != Color.BLACK) initialColor else -0xffffff
                okButton.setOnClickListener { _ ->
                    val selectedColor = picker.color
                    onColorSelected(selectedColor)
                    dialog.dismiss()
                }
            }
        }.show()
    }

    private fun showTextColorPickDialog() {
        showColorPickDialog(
            binding.edittext2.textColors.defaultColor
        ) { selectedColor ->
            binding.edittext2.setTextColor(selectedColor)
            binding.edittext2.setHintTextColor(
                Color.argb(
                    60,
                    Color.red(selectedColor),
                    Color.green(selectedColor),
                    Color.blue(selectedColor)
                )
            )
        }
    }

    private fun showSolidBackgroundColorPickDialog() {
        showColorPickDialog(
            viewModel.currentLeftGradientColor
        ) { selectedColor ->
            viewModel.setLeftGradientColor(selectedColor)
            viewModel.setRightGradientColor(selectedColor)
            viewModel.setSolidColor(selectedColor)
        }
    }

    private fun showGradientBackgroundColorPickDialog(isLeft: Boolean) {
        showColorPickDialog(
            if (isLeft) viewModel.currentLeftGradientColor else viewModel.currentRightGradientColor
        ) { selectedColor ->
            if (isLeft) {
                viewModel.setLeftGradientColor(selectedColor)
            } else {
                viewModel.setRightGradientColor(selectedColor)
            }
        }
    }

    private suspend fun setGradientBackground() {
        val gd = GradientDrawable(
            viewModel.gradientOrientation.last(),
            intArrayOf(
                viewModel.leftGradientColor.last(),
                viewModel.rightGradientColor.last()
            )
        )
        binding.root2.background = gd
    }

    private fun checkPermission(handler: PermissionHandler?) {
        val firstPermission: String
        val secondPermission: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            firstPermission = Manifest.permission.READ_MEDIA_IMAGES
            secondPermission = Manifest.permission.POST_NOTIFICATIONS
        } else {
            firstPermission = Manifest.permission.READ_EXTERNAL_STORAGE
            secondPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        val permissions = arrayOf(firstPermission, secondPermission)
        check(this, permissions, handler!!)
    }

    private fun saveAndUploadFileOnline(): Uri? {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val prefs = getSharedPreferences("login_data", MODE_PRIVATE)
        val filepath = uid + "/Images/" + prefs.getString("filename", "") + ".jpg"
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageReference = firebaseStorage.reference
        val ref = FirebaseDatabase.getInstance().reference.child(uid).child("Images")
        val path = saveBitMap(this, binding.root2)
        val mountainImagesRef = storageReference.child(filepath)
        if (path != null) {
            mountainImagesRef.putFile(path)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mountainImagesRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                            ref.child(
                                prefs.getString("filename", "") ?: ""
                            ).setValue(uri.toString())
                        }
                        Snackbar.make(binding.root2, "Uploaded Successfully", Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        Snackbar.make(binding.root2, "Uploaded Failed", Snackbar.LENGTH_LONG).show()
                    }
                }
        }
        return path
    }

    private fun createDialog(layoutId: Int, onShow: (ViewDataBinding, Dialog) -> Unit): Dialog {
        val dialog = Dialog(this, R.style.WideDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogBinding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, layoutId, null, false
        )
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        window?.attributes?.let {
            it.gravity = Gravity.BOTTOM
            it.flags = it.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        }
        onShow(dialogBinding, dialog)
        return dialog
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!binding.edittext2.isFocused) {
            super.onBackPressed()
        } else {
            binding.edittext2.clearFocus()
        }
    }
}
