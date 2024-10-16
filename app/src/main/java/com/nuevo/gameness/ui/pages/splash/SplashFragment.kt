package com.nuevo.gameness.ui.pages.splash

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.refreshtoken.RefreshTokenRequest
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentSplashBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.setLocale
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun getViewBinding() = FragmentSplashBinding.inflate(layoutInflater)

    private val viewModel by viewModels<SplashViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = false, bottomMenu = false)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SplashFragment", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.e("SplashFragment", " Success fetching fcm token")
            // Get new FCM registration token
            val token = task.result
            viewModel.setDeviceToken(token)
        })

        viewModel.publicConfiguration.fetchData()

        setLocale(viewModel.getLanguage() ?: Locale.getDefault().language)

        fetchData()

        updateUserId()

    }

    fun callUpdateTokensIfRequired() {
        if(viewModel.getUserInfo()["rememberMe"] == true || true){
            val userInfo = viewModel.getUserInfo()
            val token =  userInfo["token"] as String? ?: ""
            val refreshToken = userInfo["refreshToken"] as String? ?: ""
            val tokenExpiresAt = userInfo["tokenExpiresAt"] as String? ?: ""
            val refreshTokenExpiresAt = userInfo["refreshTokenExpiresAt"] as String? ?: ""


            if (token.isEmpty() || refreshToken.isEmpty() || tokenExpiresAt.isEmpty() || refreshToken.isEmpty()) {
                navigateToWelcomePage()
                return
            }

            val currentDate = LocalDateTime.now()

            // compare current date with refreshTokenExpiresAt
            val refreshTokenExpireDate = LocalDateTime.parse(refreshTokenExpiresAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)


            if (refreshTokenExpireDate < currentDate) {
                navigateToWelcomePage()
                return
            }

            // compare current date with tokenExpiresAt
            val tokenExpireDate = LocalDateTime.parse(tokenExpiresAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            tokenExpireDate
            if(tokenExpireDate < currentDate) {
                // call the refreshToen api
                viewModel.request = RefreshTokenRequest(
                    token,
                    refreshToken
                )
                viewModel.refreshToken.fetchData()
                return
            }

            navigate()

        } else {
            navigateToWelcomePage()
        }
    }

    private fun navigateToWelcomePage() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigate(R.id.action_splashFragment_to_welcomeFragment)
        }, 300)
    }

    private fun fetchData() {

        viewModel.publicConfiguration.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {

                    val conf = state.data.data
                    if (conf != null) {
                        viewModel.savePublicConf(conf)
                        Constants.AUTO_REFRESH_RATE = conf.autoRefreshInterval.toInt() * 1000

                        try {
                            callUpdateTokensIfRequired()
                        } catch (e: org.threeten.bp.format.DateTimeParseException) {
                            navigateToWelcomePage()
                        }

                    } else {
                        showDialog(getString(R.string.error_warning))
                    }

                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {
                    state.show()
                    showToast(getString(R.string.error_warning))

                    if (state.code == 401) {
                        navigateToWelcomePage()
                    }

                }
            }
        }

        viewModel.refreshToken.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.token != null && state.data.user?.id != null && state.data.refreshToken != null && state.data.expiresAt != null && state.data.refreshTokenExpiresAt != null) {
                        UserModel.instance.id = state.data.user.id
                        UserModel.instance.token = state.data.token
                        UserModel.instance.tokenExpiresAt = state.data.expiresAt
                        UserModel.instance.refreshToken = state.data.refreshToken
                        UserModel.instance.refreshTokenExpiresAt = state.data.refreshTokenExpiresAt
                        viewModel.saveTokens(UserModel.instance.token, UserModel.instance.refreshToken, UserModel.instance.tokenExpiresAt, UserModel.instance.refreshTokenExpiresAt)
                        viewModel.saveUserId(UserModel.instance.id)
                        navigate()
                    } else {
                        showToast(getString(R.string.error_warning))
                        navigateToWelcomePage()
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {
                    state.show()
                    showToast(getString(R.string.error_warning))

                    if(state.code == 401) {
                        navigateToWelcomePage()
                    }

                }
            }
        }
    }

    private fun navigate() {
        requireActivity().intent.apply {
            when (getStringExtra("dataType")) {
                "1" -> navigate(R.id.nav_announcements)
                "2" -> navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.refereeChatFragment,
                    "id" to getStringExtra("dataId")
                ))
                "3" -> navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentRoomHomeFragment,
                    "id" to getStringExtra("dataId"),
                    "user_id" to getStringExtra("senderUserId")
                ))
                else -> navigate(R.id.nav_home)
            }
            putExtra("dataType", -1)
        }
    }


    fun requestNotificationPermission() {

    }

    fun updateUserId() {
        UserModel.instance.id = viewModel.getUserId()
    }
}
