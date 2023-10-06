package me.nomi.urdutyper.presentation.ui.dashboard.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.BottomSheetImageBinding
import me.nomi.urdutyper.presentation.utils.common.ImageMaker
import me.nomi.urdutyper.presentation.utils.common.Notification
import me.nomi.urdutyper.presentation.utils.extensions.adapter.BaseBottomSheetDialogFragment
import me.nomi.urdutyper.presentation.utils.extensions.common.dialog
import me.nomi.urdutyper.presentation.utils.permissions.PermissionHandler
import me.nomi.urdutyper.presentation.utils.permissions.Permissions.check
import java.io.IOException

@AndroidEntryPoint
class ImageSheet : BaseBottomSheetDialogFragment<BottomSheetImageBinding>() {
    var loadImage: ((BottomSheetImageBinding) -> Unit)? = null
    var deleteImage: (() -> Unit)? = null
    var fileName = System.currentTimeMillis().toString()
    override fun layoutId(): Int = R.layout.bottom_sheet_image
    override fun onViewCreated(binding: BottomSheetImageBinding) {
        loadImage?.invoke(binding)

        binding.saveBigImages.setOnClickListener {
            checkPermission(object : PermissionHandler {
                override fun onGranted() {
                    val uri: Uri?
                    try {
                        uri = ImageMaker.getImageFromImageView(
                            requireActivity(),
                            binding.bigImage,
                            System.currentTimeMillis().toString()
                        )
                        Notification.create(
                            requireActivity(),
                            "$fileName.jpg",
                            uri
                        )
                        Toast.makeText(requireActivity(), "Saved!", Toast.LENGTH_LONG).show()
                    } catch (e: IOException) {
                        dialog(e.localizedMessage ?: "Something went wrong").show()
                    }
                }

                override fun onDenied() {
                    Toast.makeText(
                        requireActivity(),
                        "Required Permissions are denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        binding.delete.setOnClickListener {
            dialog("Are you sure want to delete this file?") {
                negativeButton("No")
                positiveButton("Yes") {
                    dismiss()
                    deleteImage?.invoke()
                }
            }.show()
        }
    }

    private fun checkPermission(handler: PermissionHandler) {
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
        check(requireActivity(), permissions.toSet().toTypedArray(), handler)
    }

}