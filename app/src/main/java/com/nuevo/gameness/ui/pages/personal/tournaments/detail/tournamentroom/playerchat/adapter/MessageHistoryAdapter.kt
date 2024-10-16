package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.readymessages.MessageHistoryItem
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.databinding.ItemReceivedMessageBinding
import com.nuevo.gameness.databinding.ItemSendMessageBinding
import com.nuevo.gameness.utils.load

class MessageHistoryAdapter(
    private val itemList: ArrayList<MessageHistoryItem> = arrayListOf()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SEND = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    class SendMessageViewHolder(
        private val binding: ItemSendMessageBinding,
        private val context: Context
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageHistoryItem) {
            binding.apply {
                root.rotation = 180F
                if (item.senderImageURL != null) imageViewProfile.load(context, item.senderImageURL)
                else imageViewProfile.setImageResource(R.drawable.default_profile)
                textViewMessage.text = item.message
            }
        }
    }

    class ReceivedMessageViewHolder(
        private val binding: ItemReceivedMessageBinding,
        private val context: Context
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageHistoryItem) {
            binding.apply {
                root.rotation = 180F
                if (item.senderImageURL != null) imageViewProfile.load(context, item.recipientImageURL)
                else imageViewProfile.setImageResource(R.drawable.default_profile)
                textViewMessage.text = item.message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].senderID == UserModel.instance.id) VIEW_TYPE_SEND
        else VIEW_TYPE_RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_SEND) SendMessageViewHolder(
            ItemSendMessageBinding.inflate(inflater, parent, false),
            context
        )
        else ReceivedMessageViewHolder(
            ItemReceivedMessageBinding.inflate(inflater, parent, false),
            context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SendMessageViewHolder -> holder.bind(itemList[position])
            is ReceivedMessageViewHolder -> holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateAllData(newList: List<MessageHistoryItem>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(position: Int, item: MessageHistoryItem) {
        itemList.add(position, item)
        notifyItemInserted(position)
    }

}