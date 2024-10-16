package com.nuevo.gameness.ui.pages.personal.profile

import com.nuevo.gameness.R
import com.nuevo.gameness.databinding.FragmentProfileBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.TablayoutAdapter
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.MyProfileFragment
import com.nuevo.gameness.ui.pages.personal.profile.myteam.MyTeamFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var adapter: TablayoutAdapter

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(visible = false)

        adapter= TablayoutAdapter(childFragmentManager)
        adapter.addFragment(MyProfileFragment(), getString(R.string.my_profile).uppercase(Locale.getDefault()))
        adapter.addFragment(MyTeamFragment(), getString(R.string.my_team).uppercase(Locale.getDefault()))

        //Adapter'ımızdaki verileri viewPager adapter'a veriyoruz
        binding.viewPager.adapter = adapter
        //Tablar arasında yani viewPager'lar arasında geçisi sağlıyoruz
        binding.profileTabs.setupWithViewPager(binding.viewPager)

        events()
    }

    private fun events() {
        binding.apply {
            imageViewSettings.setOnClickListener {
                navigate(R.id.action_profileFragment_to_nav_settings)
            }
        }
    }

}