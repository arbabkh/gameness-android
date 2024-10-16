package com.nuevo.gameness.ui.splash

import androidx.fragment.app.viewModels
import com.nuevo.gameness.base.BaseFragment
import com.nuevo.gameness.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override fun getViewBinding(): FragmentSplashBinding =
        FragmentSplashBinding.inflate(layoutInflater)

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun initViews() {

    }
}