package com.nuevo.gameness.ui.pages.personal.profile.settings.personalinfo

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.register.Country
import com.nuevo.gameness.data.model.settings.request.ChangeUserInformationRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentPersonalInfoBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<FragmentPersonalInfoBinding>() {


    override fun getViewBinding() = FragmentPersonalInfoBinding.inflate(layoutInflater)

    private val viewModel by viewModels<PersonalInfoViewModel>()
    private val calendar = Calendar.getInstance().clone() as Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.edtBirthday.text = sdf.format(calendar.time)
        }

        events()
        fetchData()
    }

    private fun events(){


        binding.apply {

            btnSave.setOnClickListener {
                val phone =  "+" + ccp.selectedCountryCode + edtPhoneNumber.text.toString()
                val birthday = edtBirthday.text.toString()
                if(phone.isEmpty() || birthday.isEmpty()){
                    showDialog(getString(R.string.empty_warning))
                } else if (!phone.matches(Constants.PHONE_NUMBER_REGEX)) {
                    showDialog(getString(R.string.phone_number_validation))
                } else {
                    viewModel.request = ChangeUserInformationRequest(
                        birthday.toCalendar("dd/MM/yyyy")?.time.myToString("yyyy-MM-dd"),
                        phone
                    )
                    viewModel.changeUserInformation.fetchData()
                }
            }

            edtBirthday.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireContext(), dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.show()
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setMyTextColor(context, R.color.blue)
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setMyTextColor(context, R.color.blue)
            }
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun setPhoneNumber(phone: String) {

        if (!phone.matches(Constants.PHONE_NUMBER_REGEX)) {
            binding.edtPhoneNumber.setText(phone)
            binding.ccp.isSelected = false
            return
        }

        // extract country code

        val phoneUtil = PhoneNumberUtil.getInstance()
        val numberProto = phoneUtil.parse(phone, "")

        val countryCode = "" + numberProto.countryCode
        val phoneNumber = phone.substring(countryCode.length + 1, phone.length)

        binding.edtPhoneNumber.setText(phoneNumber)
        binding.ccp.setCountryForPhoneCode(numberProto.countryCode)

    }
    private fun fetchData(){
        viewModel.getUser.fetchData()

        viewModel.getUser.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data?.let { userInfo ->
                            edtNameSurname.setText(userInfo.name+" "+userInfo.surname)
                            edtEposta.setText(userInfo.email?:"")
                            setPhoneNumber(userInfo.phoneNumber?:"")
                            edtBirthday.text = userInfo.birthDate?.toCalendar()?.time.myToString("dd/MM/yyyy")
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }

        viewModel.changeUserInformation.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val result=state.data.success
                        if(result){
                            showToast(getString(R.string.your_information_has_been_updated_successfully))
                            viewModel.getUser.fetchData()
                        }else{
                            val message=state.data.message
                            showToast(message.toString())
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }


        viewModel.countryList.fetchData()

        viewModel.countryList.state.observe(viewLifecycleOwner) { state ->

            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    if (response.success == true)  {
                        // set the co
                        setCountries(response.data ?: emptyArray())
                    }
                    //   else // {} // showToast(response.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading ->  {} //showLoading.value = state.isLoading
                is NetworkResult.Error ->  {}  // state.show()
            }

        }

    }

    private fun setCountries(countries: Array<Country>) {

        var code = ""

        for ((i, c) in countries.withIndex()) {
            code += if (i != countries.size - 1) {
                c.countryCode.toLowerCase() + ","
            } else {
                c.countryCode.toLowerCase()
            }
        }

        binding.ccp.setCustomMasterCountries(code)

    }

}