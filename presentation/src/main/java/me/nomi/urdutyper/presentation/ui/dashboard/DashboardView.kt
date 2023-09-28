package me.nomi.urdutyper.presentation.ui.dashboard

import me.nomi.urdutyper.domain.entity.Image

interface DashboardView {
    fun showImages(images: List<Image>)
    fun showMessageDialog(message: String)
    fun navigateToImage(image: Image)
    fun logout()
}