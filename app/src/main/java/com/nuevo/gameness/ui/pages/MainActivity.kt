package com.nuevo.gameness.ui.pages

import androidx.annotation.IdRes
import androidx.navigation.findNavController
import com.nuevo.gameness.R
import com.nuevo.gameness.databinding.ActivityMainBinding
import com.nuevo.gameness.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        events()
    }

    private fun events() {
        binding.apply {
            bottomNavigationMain.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.menu_home -> bottomMenuNavigate(R.id.nav_home)
                    R.id.menu_announcements -> bottomMenuNavigate(R.id.nav_announcements)
                    R.id.menu_tournaments -> bottomMenuNavigate(R.id.nav_tournaments)
                    R.id.menu_events -> bottomMenuNavigate(R.id.nav_events)
                    R.id.menu_profile -> bottomMenuNavigate(R.id.nav_profile)
                    else -> false
                }
            }

        }
    }

    private fun bottomMenuNavigate(@IdRes resId: Int): Boolean {
        findNavController(R.id.navHostFragment_main).navigate(resId)
        return true
    }

}