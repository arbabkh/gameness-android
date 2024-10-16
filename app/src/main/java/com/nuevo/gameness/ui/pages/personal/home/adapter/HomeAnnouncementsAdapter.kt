package com.nuevo.gameness.ui.pages.personal.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.announcements.AnnouncementItem
import com.nuevo.gameness.databinding.ItemHomeAnnouncementsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class HomeAnnouncementsAdapter : BaseRecyclerAdapter<AnnouncementItem, ItemHomeAnnouncementsBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemHomeAnnouncementsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: AnnouncementItem, binding: ItemHomeAnnouncementsBinding) {
        binding.apply {
            imageViewHomeAnnouncement.load(context, item.imageUrl)
            textViewAnnouncementTitle.text = item.title
            textViewAnnouncementDescription.text = item.description
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
        }
    }

}