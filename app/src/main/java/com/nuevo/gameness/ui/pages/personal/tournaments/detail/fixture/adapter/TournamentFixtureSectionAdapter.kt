package com.nuevo.gameness.ui.pages.personal.tournaments.detail.fixture.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentFixtureData
import com.nuevo.gameness.databinding.ItemTournamentFixtureSectionBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter

class TournamentFixtureSectionAdapter(
    private val replaceTextList: List<String>,
    private val tournamentType: Int?
) : BaseRecyclerAdapter<TournamentFixtureData, ItemTournamentFixtureSectionBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentFixtureSectionBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TournamentFixtureData, binding: ItemTournamentFixtureSectionBinding) {
        binding.apply {
            val adapter = TournamentFixtureChildAdapter()
            recyclerViewTournamentFixtureSection.adapter = adapter
            item.matches?.let { matches -> adapter.updateAllData(matches) }
            textViewTournamentFixtureSectionTitle.text =
                if (tournamentType == 3 || tournamentType == 4) "${item.stageText}. ${context.getString(R.string.round)}"
                else item.stageText
            imageViewTournamentFixtureUpArrow.setOnClickListener {
                if(recyclerViewTournamentFixtureSection.visibility == View.VISIBLE) {
                    recyclerViewTournamentFixtureSection.visibility = View.GONE
                    imageViewTournamentFixtureUpArrow.setImageResource(R.drawable.ic_bottom_arrow)
                } else {
                    recyclerViewTournamentFixtureSection.visibility= View.VISIBLE
                    imageViewTournamentFixtureUpArrow.setImageResource(R.drawable.ic_up_arrow)
                }
            }
        }
    }

    override fun updateAllData(newList: List<TournamentFixtureData>) {
        if (tournamentType == 1 || tournamentType == 2) {
            if (tournamentType == 1) newList.find { it.stageText == "-1" }?.stageText = replaceTextList[0]
            newList.find { it.stageText == "0" }?.stageText = replaceTextList[1]
            newList.find { it.stageText == "1" }?.stageText = replaceTextList[2]
            newList.find { it.stageText == "2" }?.stageText = replaceTextList[3]
            newList.find { it.stageText == "3" }?.stageText = replaceTextList[4]
        }
        super.updateAllData(newList)
    }

}