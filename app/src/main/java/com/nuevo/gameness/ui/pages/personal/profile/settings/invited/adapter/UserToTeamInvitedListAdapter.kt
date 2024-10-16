package com.nuevo.gameness.ui.pages.personal.profile.settings.invited.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.teamusers.UserToTeamInvitedListData
import com.nuevo.gameness.databinding.ItemUserToTeamInvitedListBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.setMyTextColor

class UserToTeamInvitedListAdapter :
    BaseRecyclerAdapter<UserToTeamInvitedListData, ItemUserToTeamInvitedListBinding>() {
    var onPullBackClickListener: ((pos: Int, item: UserToTeamInvitedListData) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemUserToTeamInvitedListBinding.inflate(inflater, parent, false)

    override fun bind(
        position: Int,
        item: UserToTeamInvitedListData,
        binding: ItemUserToTeamInvitedListBinding
    ) {
        binding.apply {
            imageViewUserProfile.load(context, item.userImageURL)
            textViewUsername.text = item.username
            textViewStatus.text = item.status
            if (item.statusID == 2) {
                pullBackButton.visibility = View.VISIBLE
                textViewStatus.visibility = View.GONE
                pullBackButton.setOnClickListener {
                    onPullBackClickListener?.invoke(position, item)
                }
            } else {
                pullBackButton.visibility = View.GONE
                textViewStatus.visibility = View.VISIBLE

                val resId = when (item.statusID) {
                    1 -> R.color.green // Kabul Edildi (Accepted)
                    2 -> R.color.yellow // Gönderildi (Sent)
                    -1 -> R.color.red // Reddedildi (Denied)
                    -2 -> R.color.gray // Geri Çekildi (Withdrawn)
                    else -> R.color.white // Belirtilmedi (unstated)
                }
                textViewStatus.setMyTextColor(context, resId)
            }


        }
    }

}