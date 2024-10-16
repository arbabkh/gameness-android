package com.nuevo.gameness.ui.pages.personal.tournaments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.fixture.TournamentFixtureFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.information.TournamentInformationFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.TournamentRoomFragment

class TournamentDetailAdapter(
    fragment: Fragment,
    private val bundle: Bundle
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            2 -> TournamentRoomFragment()
            1 -> TournamentFixtureFragment()
            else -> TournamentInformationFragment()
        }.apply { arguments = bundle }
    }

}