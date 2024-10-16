package com.nuevo.gameness.ui.pages.register.personalinformation

import android.annotation.SuppressLint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.register.Country
import com.nuevo.gameness.data.model.users.RegisterRequest
import com.nuevo.gameness.databinding.FragmentPersonalInformationBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.openCustomChromeTab
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PersonalInformationFragment : BaseFragment<FragmentPersonalInformationBinding>() {
    var isPasswordVisible:Boolean = false;
    var onNextPageListener: ((request: RegisterRequest) -> Unit)? = null

    override fun getViewBinding() = FragmentPersonalInformationBinding.inflate(layoutInflater)

    override fun initView() {
        events()
    }

    fun setCountryList(countries: Array<Country>) {

        var code = ""

        for ((i, c) in countries.withIndex()) {
            if (i != countries.size - 1) {
                code += c.countryCode.toLowerCase() + ","
            } else {
                code += c.countryCode.toLowerCase()
            }
        }

        binding.ccp.setCustomMasterCountries(code)

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun events() {
        binding.apply {

            termsOfUseTextView.setOnClickListener {
                val langCode = Locale.getDefault()
                val url = if (langCode?.toLanguageTag().equals("tr")) {
                    Constants.TERMS_OF_USAGE_URL + "?lang=tr}"
                } else {
                    Constants.TERMS_OF_USAGE_URL + "?lang=en"
                }
                openCustomChromeTab(requireActivity(), url)
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


            buttonPersonalInformationNext.setOnClickListener {
                val nameSurname = editTextNameSurname.text.toString()
                val email = editTextEMail.text.toString()

                val phoneNumber = "+" + ccp.selectedCountryCode + editTextPhoneNumber.text.toString()
                Log.e("PersonalInformationFragment", "events: ^" + phoneNumber)

                val userName = editTextUserName.text.toString()
                val password1 = editTextPassword.text.toString()
                val password2 = editTextPasswordAgain.text.toString()
                val termsOfUseChecked = termsOfUseCheckbox.isChecked

                val message = when {
                    nameSurname.isEmpty()
                            || email.isEmpty()
                            || phoneNumber.isEmpty()
                            || userName.isEmpty()
                            || password1.isEmpty()
                            || password2.isEmpty() -> getString(R.string.empty_warning)
                    nameSurname.split(" ").size < 2 -> getString(R.string.full_name_validation)
                    !Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches() -> getString(R.string.mail_address_validation)
                    !phoneNumber.matches(Constants.PHONE_NUMBER_REGEX) -> getString(R.string.phone_number_validation)
                    password1 != password2 -> getString(R.string.password_again_validation)
                    !Constants.isValidPassword(password1) -> getString(
                        R.string.password_validation
                    )
                    !termsOfUseChecked -> getString(R.string.terms_of_use_not_checked_warning)
                    else -> null
                }
                if (message != null) showDialog(message)
                else onNextPageListener?.invoke(
                    RegisterRequest(
                        nameSurname.substringBeforeLast(" "),
                        nameSurname.substringAfterLast(" "),
                        email,
                        phoneNumber,
                        userName,
                        password1,
                        "+" + binding.ccp.selectedCountryCode
                    )
                )
            }
        }
    }

}