package com.nuevo.gameness.ui.pages.login

import android.annotation.SuppressLint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.users.DeviceLanguageRequest
import com.nuevo.gameness.data.model.users.LoginRequest
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentLoginBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    var isPasswordVisible:Boolean = false;
    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    private val viewModel by viewModels<LoginViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = false, bottomMenu = false)
        setBackButton(true) { popBackStack() }

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.login.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    Log.e("LoginFragment", "fetchData: line 52" )
                    UserModel.instance.id=state.data.user!!.id!!
                    UserModel.instance.token = state.data.token!!
                    UserModel.instance.tokenExpiresAt = state.data.expiresAt ?: ""
                    UserModel.instance.refreshToken = state.data.refreshToken ?: ""
                    UserModel.instance.refreshTokenExpiresAt = state.data.refreshTokenExpiresAt ?: ""

                    viewModel.saveTokens(UserModel.instance.token, UserModel.instance.refreshToken, UserModel.instance.tokenExpiresAt, UserModel.instance.refreshTokenExpiresAt)
                    viewModel.saveUserId(state.data.user!!.id!!)
                    //login başarılı olduğu zaman device token göndericez
                    viewModel.deviceLanguageRequest= DeviceLanguageRequest(
                         viewModel.getDeviceToken()?:"",
                        Locale.getDefault().language,
                        true
                    )
                    viewModel.setDeviceLanguage.fetchData()
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    state.show()
                    showToast(getString(R.string.login_validation))
                }
            }
        }
        viewModel.setDeviceLanguage.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if(state.data.success == true){
                        //login başarılı olursa remember me check
                        viewModel.setUserInfo(
                            binding.editTextUserName.text.toString(),
                            binding.editTextPassword.text.toString(),
                            binding.checkRemember.isChecked
                        )
                        navigate(R.id.action_loginFragment_to_nav_home)
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    state.show()
                    showToast(getString(R.string.error_warning))
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun events() {
        binding.apply {
            buttonLogin.setOnClickListener {
                viewModel.request = LoginRequest(
                    editTextUserName.text.toString(),
                    editTextPassword.text.toString()
                )
                viewModel.login.fetchData()
            }

            editTextPassword.setOnTouchListener { v, event ->
                val action = event.action
                when(action){
                    MotionEvent.ACTION_UP -> {
                        if (event.rawX >= (editTextPassword.right - editTextPassword.compoundDrawables[2].bounds.width())){
                            val selection:Int = editTextPassword.selectionEnd;
                            if (isPasswordVisible){
                                editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24,0);
                                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance();
                                isPasswordVisible = false;
                            } else {
                                editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24,0);
                                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance();
                                isPasswordVisible = true;
                            }
                            true;
                        }
                    }
                }
                 false;
            }

            textViewForgotMyPassword.setOnClickListener {
                navigate(R.id.action_loginFragment_to_forgotMyPasswordFragment)
            }
        }
    }

}