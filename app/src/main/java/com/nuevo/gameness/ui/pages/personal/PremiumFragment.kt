package com.nuevo.gameness.ui.pages.personal

import com.nuevo.gameness.databinding.FragmentPremiumBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PremiumFragment : BaseFragment<FragmentPremiumBinding>() {

    override fun getViewBinding() = FragmentPremiumBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = false, bottomMenu = false)
        setBackButton(true) { popBackStack() }

        arguments?.let { it
            binding.apply {
                val textData = it.getString("title")
                barTitle.text = textData
            }
        }
    }
}