package com.nuevo.gameness.ui.pages.personal.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.profile.AwardsItem
import com.nuevo.gameness.databinding.ItemUserAwardsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class AwardsAdapter: BaseRecyclerAdapter<AwardsItem, ItemUserAwardsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemUserAwardsBinding = ItemUserAwardsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: AwardsItem, binding: ItemUserAwardsBinding) {
       binding.apply {
           root.setOnClickListener {
               onItemClickListener?.invoke(position, item)
           }
           imageViewAward.load(context, item.iconImageURL)
           textViewAwardTitle.text = item.awardName
           textViewAwardDescription.text = item.gameName
       }
    }

}
