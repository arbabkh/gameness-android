package com.nuevo.gameness.ui.pages.personal.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.profile.AwardsItem
import com.nuevo.gameness.databinding.ItemAllAwardsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class AllAwardsAdapter : BaseRecyclerAdapter<AwardsItem, ItemAllAwardsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAllAwardsBinding = ItemAllAwardsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: AwardsItem, binding: ItemAllAwardsBinding) {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
            imgAward.load(context ,item.iconImageURL)
            txtAwardTitle.text = item.gameName
            txtAwardDesc.text = item.awardName
        }
    }

}