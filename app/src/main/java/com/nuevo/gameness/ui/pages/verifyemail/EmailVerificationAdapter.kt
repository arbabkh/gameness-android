package com.nuevo.gameness.ui.pages.verifyemail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class EmailVerificationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val sendEmailFragment = SendEmailFragment()
    val verifyEmailOtpFragment = VerifyEmailOtpFragment()



    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> sendEmailFragment
            1 -> verifyEmailOtpFragment
            else -> sendEmailFragment
        }
    }
}
