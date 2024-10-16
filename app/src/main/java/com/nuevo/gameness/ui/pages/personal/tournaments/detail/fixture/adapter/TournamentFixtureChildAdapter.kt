package com.nuevo.gameness.ui.pages.personal.tournaments.detail.fixture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.tournaments.TournamentFixtureMatches
import com.nuevo.gameness.databinding.ItemTournamentFixtureChildBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class TournamentFixtureChildAdapter : BaseRecyclerAdapter<TournamentFixtureMatches, ItemTournamentFixtureChildBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentFixtureChildBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TournamentFixtureMatches, binding: ItemTournamentFixtureChildBinding) {
        binding.apply {
            imageViewFixtureHomeName.load(context, item.homeImageUrl)
            imageViewFixtureAwayName.load(context, item.awayImageUrl)
            textViewFixtureMatchNumber.text = (item.matchNumber ?: "-").toString()
            textViewFixtureHomeName.text = item.homeName ?: "-"
            textViewFixtureAwayName.text = item.awayName ?: "-"
            textViewFixtureHomeScore.text = (item.homeScore ?: "-").toString()
            textViewFixtureAwayScore.text = (item.awayScore ?: "-").toString()
            if (item.homeScore != null && item.awayScore != null) {
                if (item.homeScore < item.awayScore) {
                    imageViewFixtureHomeName.alpha = 0.3f
                    imageViewFixtureAwayName.alpha = 1f
                    textViewFixtureHomeName.alpha = 0.3f
                    textViewFixtureAwayName.alpha = 1f
                } else if (item.homeScore > item.awayScore) {
                    imageViewFixtureHomeName.alpha = 1f
                    imageViewFixtureAwayName.alpha = 0.3f
                    textViewFixtureHomeName.alpha = 1f
                    textViewFixtureAwayName.alpha = 0.3f
                }
            } else {
                imageViewFixtureHomeName.alpha = 1f
                imageViewFixtureAwayName.alpha = 1f
                textViewFixtureHomeName.alpha = 1f
                textViewFixtureAwayName.alpha = 1f
            }
        }
    }

}