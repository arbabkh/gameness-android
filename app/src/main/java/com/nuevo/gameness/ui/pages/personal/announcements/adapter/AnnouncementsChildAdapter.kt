package com.nuevo.gameness.ui.pages.personal.announcements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.announcements.AnnouncementItem
import com.nuevo.gameness.databinding.ItemAnnouncementsChildBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class AnnouncementsChildAdapter : BaseRecyclerAdapter<AnnouncementItem, ItemAnnouncementsChildBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemAnnouncementsChildBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: AnnouncementItem, binding: ItemAnnouncementsChildBinding) {
        binding.apply {
            imageViewAnnouncementIcon.load(context, item.iconUrl)
            textViewAnnouncementsTitle.text = item.title
            textViewAnnouncementsDescription.text = item.description
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
        }
    }

}