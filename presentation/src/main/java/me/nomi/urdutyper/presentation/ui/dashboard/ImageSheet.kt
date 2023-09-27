package me.nomi.urdutyper.presentation.ui.dashboard

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.BottomSheetImageBinding
import me.nomi.urdutyper.presentation.utils.extensions.adapter.BaseBottomSheetDialogFragment
import java.io.IOException

@AndroidEntryPoint
class ImageSheet : BaseBottomSheetDialogFragment<BottomSheetImageBinding>() {
    var loadImage: ((BottomSheetImageBinding) -> Unit)? = null
    override fun layoutId(): Int = R.layout.bottom_sheet_image
    override fun onViewCreated(binding: BottomSheetImageBinding) {
        loadImage?.invoke(binding)
        binding.delete.isVisible = false

        binding.saveBigImages.setOnClickListener {
            if (checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireActivity(),
                    "Please allow access to storage then try again",
                    Toast.LENGTH_LONG
                ).show()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101
                )
            } else {
                var uri: Uri? = null
                try {
                    uri = ImageMaker.getImageFromImageView(
                        requireActivity(),
                        binding.bigImage,
                        System.currentTimeMillis().toString()
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Notification.create(
                    requireActivity(),
                    System.currentTimeMillis().toString() + ".jpg",
                    uri
                )
                Toast.makeText(requireActivity(), "Saved!", Toast.LENGTH_LONG).show()
            }
        }
    }

}