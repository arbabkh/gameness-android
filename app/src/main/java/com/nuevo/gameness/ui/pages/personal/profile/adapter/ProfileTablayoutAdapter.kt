package com.nuevo.gameness.ui.pages.personal.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.achievements.AchievementsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.attendtournaments.AttendTournamentsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.discover.DiscoverFragment

class ProfileTablayoutAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()


    //Fragment'in pozisyonunu veriyoruz
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = DiscoverFragment()
            1 -> fragment = AchievementsFragment()
            2 -> fragment = AttendTournamentsFragment()
            else -> {}
        }

        return fragment!!
    }
    //Fragment sayısını veriyoruz
    override fun getCount(): Int {
        return fragmentList.size
    }
    //Bu fonksiyon ile Fragment'leri ve title'ları ekliyoruz
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
    //Title'ların pozisyonunu veriyoruz
    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }


}

