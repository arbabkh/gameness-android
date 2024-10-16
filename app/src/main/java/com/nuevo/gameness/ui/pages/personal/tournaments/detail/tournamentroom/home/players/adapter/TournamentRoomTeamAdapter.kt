package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.players.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournamentrooms.NextMatchTeamData
import com.nuevo.gameness.data.model.tournamentrooms.NextMatchTeamUserData
import com.nuevo.gameness.databinding.ItemTournamentRoomTeamBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class TournamentRoomTeamAdapter : BaseRecyclerAdapter<NextMatchTeamData, ItemTournamentRoomTeamBinding>() {

    var onPlayerClickListener: ((pos: Int, item: NextMatchTeamUserData) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentRoomTeamBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: NextMatchTeamData, binding: ItemTournamentRoomTeamBinding) {
        binding.apply {
            val adapter = TournamentRoomPlayersAdapter()
            recyclerViewTournamentRoomTeam.adapter = adapter
            item.teamUsers?.let { adapter.updateAllData(it) }
            textViewTournamentRoomTeamName.text = item.teamName
            imageViewIcForOpenList.setOnClickListener {
                if (recyclerViewTournamentRoomTeam.visibility == View.VISIBLE) {
                    recyclerViewTournamentRoomTeam.visibility = View.GONE
                    imageViewIcForOpenList.setImageResource(R.drawable.ic_bottom_arrow)
                } else {
                    recyclerViewTournamentRoomTeam.visibility = View.VISIBLE
                    imageViewIcForOpenList.setImageResource(R.drawable.ic_up_arrow)
                }
            }
            adapter.onItemClickListener = { pos, item ->
                onPlayerClickListener?.invoke(pos, item)
            }
        }
    }

}