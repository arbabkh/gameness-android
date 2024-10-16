package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.scoreboard

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.tournamentrooms.ScoreboardData
import com.nuevo.gameness.databinding.ItemTournamentRoomScoreboardBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class TournamentRoomScoreboardAdapter: BaseRecyclerAdapter<ScoreboardData, ItemTournamentRoomScoreboardBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentRoomScoreboardBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: ScoreboardData, binding: ItemTournamentRoomScoreboardBinding) {
        binding.apply {
            imageViewScoreboardLogo.load(context, item.participantImageUrl)
            textViewOrder.text = (position + 1).toString()
            textViewParticipantName.text = item.participantName
            textViewOM.text = item.playedMatchCount.toString()
            textViewKZ.text = item.wonCount.toString()
            textViewKY.text = item.lostCount.toString()
            textViewBB.text = item.drawCount.toString()
            textViewP.text = item.score.toString()
        }
    }

}