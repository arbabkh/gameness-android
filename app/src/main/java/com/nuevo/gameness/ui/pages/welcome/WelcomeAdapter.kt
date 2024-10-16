package com.nuevo.gameness.ui.pages.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.sliders.SliderListItem
import com.nuevo.gameness.databinding.ItemWelcomeBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class WelcomeAdapter : BaseRecyclerAdapter<SliderListItem, ItemWelcomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemWelcomeBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: SliderListItem, binding: ItemWelcomeBinding) {
        binding.apply {
            textViewWelcomeTitle.text = item.title
            textViewWelcomeDescription.text = item.description
        }
    }

}