package com.nuevo.gameness.ui.pages.verifyphone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PhoneVerificationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    val sendSmsFragment = SendSmsFragment()
    val verifyOtpFragment = VerifySmsOtpFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> sendSmsFragment
            1 -> verifyOtpFragment
            else -> verifyOtpFragment
        }
    }

}