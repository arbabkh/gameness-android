package com.nuevo.gameness.ui.pages.personal.announcements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.announcements.AnnouncementItem
import com.nuevo.gameness.data.model.announcements.CategorizedAnnouncements
import com.nuevo.gameness.databinding.ItemAnnouncementsSectionBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class AnnouncementsSectionAdapter : BaseRecyclerAdapter<CategorizedAnnouncements,ItemAnnouncementsSectionBinding>() {

    var onChildItemClickListener: ((pos: Int, item: AnnouncementItem) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemAnnouncementsSectionBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: CategorizedAnnouncements, binding: ItemAnnouncementsSectionBinding) {
        binding.apply {
            textViewAnnouncementsSectionTitle.text = item.title
            val adapter = AnnouncementsChildAdapter()
            adapter.updateAllData(item.announcementList)
            adapter.onItemClickListener = { pos, item ->
                onChildItemClickListener?.invoke(pos, item)
            }
            recyclerViewAnnouncementsSection.adapter = adapter
        }
    }
}