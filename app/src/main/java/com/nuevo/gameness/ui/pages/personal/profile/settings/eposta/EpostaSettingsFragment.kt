package com.nuevo.gameness.ui.pages.personal.profile.settings.eposta

import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.request.ChangeEmailRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentEpostaSettingsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpostaSettingsFragment:BaseFragment<FragmentEpostaSettingsBinding>() {

    override fun getViewBinding(): FragmentEpostaSettingsBinding =
        FragmentEpostaSettingsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<EpostaSettingsViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            btnSave.setOnClickListener {
                val oldMail = edtEposta.text.toString()
                val newMail = edtNewEposta.text.toString()
                when {
                    newMail.isEmpty() -> showDialog(getString(R.string.empty_warning))
                    !Patterns.EMAIL_ADDRESS.matcher(newMail).matches() ->
                        showDialog(getString(R.string.mail_address_validation))
                    else -> {
                        viewModel.request = ChangeEmailRequest(oldMail, newMail)
                        viewModel.changeEmail.fetchData()
                    }
                }
            }
            icBack.setOnClickListener { popBackStack() }
        }

    }
    private fun fetchData() {
        viewModel.getUser.fetchData()

        viewModel.changeEmail.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success) {
                        showToast(getString(R.string.your_information_has_been_updated_successfully))
                        Handler(Looper.getMainLooper()).postDelayed({
                            popBackStack()
                        }, 10)
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getUser.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.data?.let { user ->
                        binding.edtEposta.setText(user.email)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

}