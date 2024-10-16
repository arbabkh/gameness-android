package com.nuevo.gameness.ui.pages.welcome

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.sliders.SliderListItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentWelcomeBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    private val viewModel by viewModels<WelcomeViewModel>()
    private val adapter = WelcomeAdapter()

    override fun getViewBinding() = FragmentWelcomeBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = false)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun fetchData() {
        viewModel.getSliderList.fetchData()

        viewModel.getSliderList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        adapter.updateAllData(items as ArrayList<SliderListItem>)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {


            if (viewModel.isSignupEnabled) {
                buttonWelcomeRegister.visibility = View.VISIBLE
            } else {
                buttonWelcomeRegister.visibility = View.GONE
            }

            txtLanguage.text = Locale.getDefault().language.uppercase(Locale.getDefault())
//            viewPager2Welcome.adapter = adapter

//            TabLayoutMediator(tabLayoutWelcome, viewPager2Welcome)
//            { _, _ ->}.attach()

            buttonWelcomeRegister.setOnClickListener {
                navigate(R.id.action_welcomeFragment_to_registerFragment)
            }
            buttonWelcomeLogin.setOnClickListener {
                navigate(R.id.action_welcomeFragment_to_loginFragment)
            }

            txtLanguage.setOnClickListener {
                showLanguageDialog()
            }
        }
    }

    private fun showLanguageDialog(){
        val languageDialog = Dialog(requireContext())
        languageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        languageDialog.setCancelable(true)

        val d: Drawable = ColorDrawable(Color.BLACK)
        d.alpha = 200
        languageDialog.window!!.setBackgroundDrawable(d)
        languageDialog.setContentView(R.layout.dialog_language)
        val txtTR=languageDialog.findViewById(R.id.txtTR) as TextView
        val txtEN=languageDialog.findViewById(R.id.txtEN) as TextView

        txtTR.setOnClickListener {
            viewModel.setLanguage("tr")
            setLocale("tr")
            languageDialog.dismiss()
            popBackStack()
            navigate(R.id.welcomeFragment)
        }
        txtEN.setOnClickListener {
            viewModel.setLanguage("en")
            setLocale("en")
            languageDialog.dismiss()
            popBackStack()
            navigate(R.id.welcomeFragment)
        }

        languageDialog.show()
    }

}