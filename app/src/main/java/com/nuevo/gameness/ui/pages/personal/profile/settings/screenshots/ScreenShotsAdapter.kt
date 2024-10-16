package com.nuevo.gameness.ui.pages.personal.profile.settings.screenshots

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.settings.ScreenShotItem
import com.nuevo.gameness.databinding.ItemScreenshotBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class ScreenShotsAdapter(
    private val margin: Int = 10,
    private val isSelectable: Boolean? = null
) : BaseRecyclerAdapter<ScreenShotItem, ItemScreenshotBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemScreenshotBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: ScreenShotItem, binding: ItemScreenshotBinding) {
        binding.apply {

        }
    }
}