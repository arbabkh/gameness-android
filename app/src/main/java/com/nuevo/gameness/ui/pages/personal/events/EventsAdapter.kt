package com.nuevo.gameness.ui.pages.personal.events

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.events.EventItem
import com.nuevo.gameness.databinding.ItemEventBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.toDate

class EventsAdapter : BaseRecyclerAdapter<EventItem, ItemEventBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemEventBinding.inflate(inflater,parent,false)

    override fun bind(position: Int, item: EventItem, binding: ItemEventBinding) {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
            imgEvents.load(context, item.imageURL)
            txtTitle.text = item.title
            txtDescription.text = item.shortDescription
            item.eventDate?.let { txtDate.text = toDate(it.split("T")[0]) }
        }
    }

}