package me.nomi.urdutyper.ui.dashboard

import com.bumptech.glide.Glide
import me.nomi.urdutyper.R
import me.nomi.urdutyper.databinding.SinglerowBinding
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.utils.extensions.adapter.BaseAdapter

class DashboardAdapter : BaseAdapter<Image, SinglerowBinding>(R.layout.singlerow) {
    override fun bind(binding: SinglerowBinding, item: Image) {
        binding.textView.text = item.fileName
        Glide.with(binding.root.context)
            .load(item.url)
            .placeholder(R.drawable.not_found)
            .centerInside()
            .into(binding.imageView)
    }
}
