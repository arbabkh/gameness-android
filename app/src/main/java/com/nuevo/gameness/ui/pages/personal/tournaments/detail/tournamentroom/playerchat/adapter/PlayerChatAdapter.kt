package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.readymessages.ReadyMessageInboxData
import com.nuevo.gameness.databinding.ItemPlayerChatBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.myToString
import com.nuevo.gameness.utils.toCalendar

class PlayerChatAdapter : BaseRecyclerAdapter<ReadyMessageInboxData, ItemPlayerChatBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemPlayerChatBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: ReadyMessageInboxData, binding: ItemPlayerChatBinding) {
        binding.apply {
            imageViewPlayer.load(context, item.chatUserImageUrl)
            textViewUserName.text = item.chatUserName
            textViewMessage.text = item.lastMessage
            textViewMessageDate.text = item.lastMessageDate.toCalendar()?.time.myToString("dd/MM/yyyy")
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
                root.setBackgroundColor(ContextCompat.getColor(context, R.color.tertiary_dark))
            }
            if (item.isReaded == true) root.setBackgroundColor(ContextCompat.getColor(context, R.color.tertiary_dark))
            else root.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        }
    }

}