package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.readymessages.MessageItem
import com.nuevo.gameness.databinding.ItemReadyMessageBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class ReadyMessageAdapter : BaseRecyclerAdapter<MessageItem, ItemReadyMessageBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemReadyMessageBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: MessageItem, binding: ItemReadyMessageBinding) {
        binding.root.text = item.name
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(position, item)
        }
    }

}