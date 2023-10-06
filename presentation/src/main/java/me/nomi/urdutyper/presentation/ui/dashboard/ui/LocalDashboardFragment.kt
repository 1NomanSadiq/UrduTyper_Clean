package me.nomi.urdutyper.presentation.ui.dashboard.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.FragmentLocalDashboardBinding
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.presentation.app.base.BaseFragment
import me.nomi.urdutyper.presentation.ui.dashboard.view.DashboardView
import me.nomi.urdutyper.presentation.utils.common.ImageMaker.getListOfImages
import me.nomi.urdutyper.presentation.utils.extensions.adapter.attach
import me.nomi.urdutyper.presentation.utils.extensions.common.dialog


@AndroidEntryPoint
class LocalDashboardFragment : BaseFragment<FragmentLocalDashboardBinding>(), DashboardView {
    private lateinit var imageSheet: ImageSheet
    private val adapter by lazy { DashboardAdapter() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentLocalDashboardBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        binding.recView.attach(
            layoutManager = GridLayoutManager(requireActivity(), 3),
            adapter = adapter,
            onItemClick = { _, item ->
                openImage(item)
            }
        )
    }

    override fun showImages(images: List<Image>) {
        adapter.pushData(images)
    }

    override fun showMessageDialog(message: String) {
        dialog(message).show()
    }

    override fun openImage(image: Image) {
        imageSheet = ImageSheet()
        imageSheet.loadImage = {
            it.saveBigImages.isVisible = false
            Glide.with(it.root.context)
                .load(image.url)
                .error(R.drawable.not_found)
                .into(it.bigImage)
        }
        imageSheet.fileName = image.fileName
        imageSheet.deleteImage = {
            deleteFile(image.url.toUri())
            dismissSheet()
        }
        imageSheet.show(
            requireActivity().supportFragmentManager,
            "openImageSheet"
        )
    }

    private fun deleteFile(contentUri: Uri): Boolean {
        return try {
            val deleteResult = context?.contentResolver?.delete(contentUri, null, null)
            if (deleteResult == -1) {
                dialog("Error deleting file").show()
                return false
            } else {
                showImages(getListOfImages(requireActivity()))
                return true
            }
        } catch (e: Exception) {
            dialog(e.localizedMessage ?: "Something went wrong").show()
            false
        }
    }


    private fun dismissSheet() {
        imageSheet.dismiss()
    }

    override fun onResume() {
        super.onResume()
        showImages(getListOfImages(requireActivity()))
    }
}