package com.nuevo.gameness.ui.pages.register

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.register.Country
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentRegisterBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.setTabColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun getViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)

    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var registerAdapter: RegisterAdapter

    override fun initView() {
        setBottomVisibility(bottomLine = false, bottomMenu = false)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun fetchData() {

        viewModel.countryList.fetchData()

        viewModel.register.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    when {
                        response.success == true && response.data?.token != null -> {
                            UserModel.instance.token = response.data.token
                            UserModel.instance.tokenExpiresAt = response.data.expiresAt ?: ""
                            UserModel.instance.refreshToken = response.data.refreshToken ?: ""
                            UserModel.instance.refreshTokenExpiresAt = response.data.refreshTokenExpiresAt ?: ""
                            viewModel.saveTokens(UserModel.instance.token, UserModel.instance.refreshToken, UserModel.instance.tokenExpiresAt, UserModel.instance.refreshTokenExpiresAt)
                            viewModel.setUserInfo(viewModel.registerRequest?.userName ?: "", viewModel.registerRequest?.password ?: "", true)
                            viewModel.saveUserId(response.data.id ?: "")
                            viewModel.setUserGamesForRegister.fetchData()
                        }

                        UserModel.instance.token.isEmpty() -> {
                            binding.viewPager2Register.currentItem = 0
                            showToast(state.data.message.toString())
                        }
                        else -> viewModel.setUserGamesForRegister.fetchData()
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.setUserGamesForRegister.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    if (response.success == true) navigate(R.id.action_registerFragment_to_nav_home)
                    else showToast(response.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.countryList.state.observe(viewLifecycleOwner) { state ->

            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    if (response.success == true)  {
                        // set the co
                        registerAdapter.personalInformationFragment.setCountryList(response.data ?: emptyArray<Country>())
                    }
                 //   else // {} // showToast(response.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading ->  {} //showLoading.value = state.isLoading
                is NetworkResult.Error ->  {}  // state.show()
            }

        }

    }

    private fun events() {
        binding.apply {
            registerAdapter = RegisterAdapter(this@RegisterFragment)
            viewPager2Register.adapter = registerAdapter
            viewPager2Register.isUserInputEnabled = false

            registerAdapter.personalInformationFragment.onNextPageListener = { request ->
                viewPager2Register.currentItem++
                viewModel.registerRequest = request
            }

            registerAdapter.chooseGameFragment.onNextPageListener = { requestList ->
                viewPager2Register.currentItem++

                requestList.forEach {
                    it.uniqueID = ""
                }

                registerAdapter.gameInformationFragment.selectedGameList = requestList
                registerAdapter.gameInformationFragment.updateView()
            }

            registerAdapter.gameInformationFragment.onNextButtonClickListener = { request ->
                viewModel.userGamesForRegisterRequest = request
                viewModel.register.fetchData()
            }

            viewPager2Register.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position) {
                        0 -> {
                            context.setTabColor(textViewPersonalInformation, linePersonalInformation, R.color.white)
                            context.setTabColor(textViewChooseGame, lineChooseGame, R.color.tertiary_dark)
                            context.setTabColor(textViewGameInformation, lineGameInformation, R.color.tertiary_dark)
                        }
                        1 -> {
                            context.setTabColor(textViewPersonalInformation, linePersonalInformation, R.color.green)
                            context.setTabColor(textViewChooseGame, lineChooseGame, R.color.blue)
                            context.setTabColor(textViewGameInformation, lineGameInformation, R.color.tertiary_dark)
                        }
                        2 -> {
                            context.setTabColor(textViewPersonalInformation, linePersonalInformation, R.color.green)
                            context.setTabColor(textViewChooseGame, lineChooseGame, R.color.green)
                            context.setTabColor(textViewGameInformation, lineGameInformation, R.color.blue)
                        }
                    }
                }
            })

            imageViewBack.setOnClickListener { onBackPressed() }

            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = onBackPressed()
            })
        }
    }

    private fun onBackPressed() {
        binding.apply {
            if (viewPager2Register.currentItem == 0) popBackStack()
            else viewPager2Register.currentItem--
        }
    }

}