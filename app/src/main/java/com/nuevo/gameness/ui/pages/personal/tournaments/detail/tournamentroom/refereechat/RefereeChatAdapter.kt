package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.refereechat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nuevo.gameness.data.model.tournamentrefereemessages.TournamentRefereeUserChatItem
import com.nuevo.gameness.databinding.ItemReceivedMessageBinding
import com.nuevo.gameness.databinding.ItemSendImageBinding
import com.nuevo.gameness.databinding.ItemSendMessageNoProfileBinding
import com.nuevo.gameness.utils.load

class RefereeChatAdapter(
    private val itemList: ArrayList<TournamentRefereeUserChatItem> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SEND_MESSAGE = 1
        private const val VIEW_TYPE_SEND_IMAGE = 2
        private const val VIEW_TYPE_RECEIVER = 3
    }

    class SendMessageViewHolder(
        private val binding: ItemSendMessageNoProfileBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TournamentRefereeUserChatItem) {
            binding.apply {
                root.rotation = 180F
                textViewMessage.text = item.message
            }
        }
    }

    inner class SendImageViewHolder(
        private val binding: ItemSendImageBinding,
        private val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TournamentRefereeUserChatItem) {
            binding.apply {
                root.rotation = 180F
                imageViewMessage.load(context, item.message)
            }
        }
    }

    class ReceivedMessageViewHolder(
        private val binding: ItemReceivedMessageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TournamentRefereeUserChatItem) {
            binding.apply {
                root.rotation = 180F
                textViewMessage.text = item.message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isAdminResponse == false) {
            if (itemList[position].messageType == 2) VIEW_TYPE_SEND_IMAGE
            else VIEW_TYPE_SEND_MESSAGE
        } else VIEW_TYPE_RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            VIEW_TYPE_SEND_MESSAGE -> SendMessageViewHolder(
                ItemSendMessageNoProfileBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_SEND_IMAGE -> SendImageViewHolder(
                ItemSendImageBinding.inflate(inflater, parent, false),
                context
            )
            else -> ReceivedMessageViewHolder(
                ItemReceivedMessageBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SendMessageViewHolder -> holder.bind(itemList[position])
            is SendImageViewHolder -> holder.bind(itemList[position])
            is ReceivedMessageViewHolder -> holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateAllData(newList: List<TournamentRefereeUserChatItem>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(position: Int, item: TournamentRefereeUserChatItem) {
        itemList.add(position, item)
        notifyItemInserted(position)
    }

}