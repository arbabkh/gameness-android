package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.fixture.TournamentRoomFixtureFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.info.TournamentRoomInfoFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.players.TournamentRoomPlayersFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.scoreboard.TournamentRoomScoreboardFragment

class TournamentRoomHomeAdapter(
    fragment: Fragment,
    private val bundle: Bundle,
    private val tournamentType: Int?
) : FragmentStateAdapter(fragment) {

    val playersFragment = TournamentRoomPlayersFragment()

    override fun getItemCount(): Int {
        return if (tournamentType == 3 || tournamentType == 4) 4
        else 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            3 -> TournamentRoomScoreboardFragment()
            2 -> TournamentRoomFixtureFragment()
            1 -> playersFragment
            else -> TournamentRoomInfoFragment()
        }.apply { arguments = bundle }
    }

}