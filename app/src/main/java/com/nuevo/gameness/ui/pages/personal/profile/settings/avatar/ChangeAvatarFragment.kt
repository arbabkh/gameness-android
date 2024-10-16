package com.nuevo.gameness.ui.pages.personal.profile.settings.avatar

import androidx.fragment.app.viewModels
import com.nuevo.gameness.databinding.FragmentChangeAvatarBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeAvatarFragment:BaseFragment<FragmentChangeAvatarBinding>() {

    override fun getViewBinding(): FragmentChangeAvatarBinding =
        FragmentChangeAvatarBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ChangeAvatarViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData(){

    }

}