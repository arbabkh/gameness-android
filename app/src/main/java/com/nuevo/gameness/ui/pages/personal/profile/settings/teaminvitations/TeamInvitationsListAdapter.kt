package com.nuevo.gameness.ui.pages.personal.profile.settings.teaminvitations

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.settings.TeamInvitationItem
import com.nuevo.gameness.databinding.ItemTeamInvitationBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class TeamInvitationsListAdapter : BaseRecyclerAdapter<TeamInvitationItem, ItemTeamInvitationBinding>(){

    var onAcceptInvitationClickListener: ((pos: Int, item: TeamInvitationItem) -> Unit)? = null
    var onRejectInvitationClickListener: ((pos: Int, item: TeamInvitationItem) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTeamInvitationBinding.inflate(inflater, parent, false)

    override fun bind(
        position: Int,
        item: TeamInvitationItem,
        binding: ItemTeamInvitationBinding
    ) {
        binding.apply {
            teamImageView.load(context, item.iconUrl)
            teamNameTextView.text = item.teamName
            acceptInvitation.setOnClickListener {
                onAcceptInvitationClickListener?.invoke(position,item)
            }
            rejectInvitation.setOnClickListener {
                onRejectInvitationClickListener?.invoke(position,item)
            }
        }
    }

}