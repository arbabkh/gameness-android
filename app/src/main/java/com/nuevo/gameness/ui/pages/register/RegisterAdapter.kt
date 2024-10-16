package com.nuevo.gameness.ui.pages.register

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameFragment
import com.nuevo.gameness.ui.pages.register.gameinformation.GameInformationFragment
import com.nuevo.gameness.ui.pages.register.personalinformation.PersonalInformationFragment

class RegisterAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    val gameInformationFragment = GameInformationFragment()
    val chooseGameFragment = ChooseGameFragment()
    val personalInformationFragment = PersonalInformationFragment()

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            2 -> gameInformationFragment
            1 -> chooseGameFragment
            else -> personalInformationFragment
        }
    }

}