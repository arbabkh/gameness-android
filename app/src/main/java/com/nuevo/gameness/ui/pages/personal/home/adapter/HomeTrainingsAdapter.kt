package com.nuevo.gameness.ui.pages.personal.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.databinding.ItemHomeTrainingsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class HomeTrainingsAdapter : BaseRecyclerAdapter<Boolean, ItemHomeTrainingsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemHomeTrainingsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: Boolean, binding: ItemHomeTrainingsBinding) {
        binding.apply {
            root.setOnClickListener { onItemClickListener?.invoke(position, item) }
        }
    }

}