package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.players.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.tournamentrooms.NextMatchTeamUserData
import com.nuevo.gameness.databinding.ItemTournamentRoomPlayersBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class TournamentRoomPlayersAdapter : BaseRecyclerAdapter<NextMatchTeamUserData, ItemTournamentRoomPlayersBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentRoomPlayersBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: NextMatchTeamUserData, binding: ItemTournamentRoomPlayersBinding) {
        binding.apply {
            imageViewTournamentRoomPlayer.load(context, item.userImageUrl)
            textViewTournamentRoomPlayerName.text = item.userName

            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
        }
    }

}