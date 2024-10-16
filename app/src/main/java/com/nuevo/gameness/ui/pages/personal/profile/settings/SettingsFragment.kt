package com.nuevo.gameness.ui.pages.personal.profile.settings

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentSettingsBinding
import com.nuevo.gameness.ui.pages.MainActivity
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showQuestionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<SettingsViewModel>()

    private var emailAddress: String = ""
    private var phoneNumber: String = ""

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)
        events()
        fetchData()
    }

    private fun updateEmailAndPhoneTextVisibility(emailConfirmed: Boolean, phoneConfirmed: Boolean) {
        Log.e("SettingsFragment", "called update email and phgone")
        val emilVerificationRequired = viewModel.emilVerificationRequired
        val phoneVerificationRequired = viewModel.phoneVerificationRequired

        binding.txtVerifyEmail.visibility = if (!emilVerificationRequired || emailConfirmed) View.GONE else View.VISIBLE
        binding.txtVerifyPhone.visibility = if (!phoneVerificationRequired || phoneConfirmed) View.GONE else View.VISIBLE

    }

    fun fetchData() {

        viewModel.user.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data?.let { user ->


                            val emailConfirmed = user.emailConfirmed ?: false
                            val phoneConfirmed = user.phoneNumberConfirmed ?: false
                            this@SettingsFragment.emailAddress = user.email ?: ""
                            this@SettingsFragment.phoneNumber = user.phoneNumber ?: ""

                            updateEmailAndPhoneTextVisibility(emailConfirmed, phoneConfirmed)



                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

    }
    private fun events(){
        binding.apply {
            if(!UserModel.instance.isTeamLeader){
                team.visibility=View.GONE
                txtTeamInfo.visibility=View.GONE
                txtGamerSettings.visibility=View.GONE
                txtInvitedPlayers.visibility=View.GONE
            }


            if (viewModel.emilVerificationRequired) {
                txtVerifyEmail.visibility = View.VISIBLE
            } else {
                txtVerifyEmail.visibility = View.GONE
            }

            if (viewModel.phoneVerificationRequired) {
                txtVerifyPhone.visibility = View.VISIBLE
            } else {
                txtVerifyPhone.visibility = View.GONE
            }

            txtPersonalInfo.setOnClickListener {
                //Kişisel Bilgiler
                navigate(R.id.action_settingsFragment_to_personalInfoFragment)
            }
            txtPasswordSettings.setOnClickListener {
                //Şifre Ayarları
                navigate(R.id.action_settingsFragment_to_passwordSettingsFragment)
            }
            txtEmailSettings.setOnClickListener {
                //E-Posta Ayarları
                navigate(R.id.action_settingsFragment_to_epostaSettingsFragment)
            }
            txtChangeUsername.setOnClickListener {
                //Kullanıcı Adı Değiştir
                navigate(R.id.action_settingsFragment_to_changeUserNameFragment)
            }
            txtGameSettings.setOnClickListener {
                //Oyun Ayarları
                navigate(R.id.action_settingsFragment_to_gameSettingsFragment)
            }
            txtVerifyPhone.setOnClickListener {
                navigate(R.id.action_settingsFragment_to_verifyPhoneNumberFragment, bundleOf("phoneNumber" to phoneNumber))
            }

            txtVerifyEmail.setOnClickListener {
                navigate(R.id.action_settingsFragment_to_verifyEmailFragment, bundleOf("emailAddress" to emailAddress))
            }

            txtChangeAvatar.setOnClickListener {
                //Simge Değiştir
                navigate(R.id.action_settingsFragment_to_iconChangeFragment)
            }
            txtInvitePlayer.setOnClickListener {
                //Davet Et
                navigate(R.id.action_settingsFragment_to_inviteExternalUserFragment)
            }
            txtTeamInfo.setOnClickListener {
                //Takım Bilgileri
                navigate(R.id.action_settingsFragment_to_teamSettingsFragment)
            }
            txtGamerSettings.setOnClickListener {
                //Oyuncu Ayarları
                navigate(R.id.action_settingsFragment_to_gamerSettingsFragment)
            }
            txtInvitedPlayers.setOnClickListener {
                //Davet Ettiğim Oyuncular
                navigate(R.id.action_settingsFragment_to_invitedGamersFragment)
            }
            teamInvitationsTextView.setOnClickListener {
                //Gelen Takım Davetleri
                navigate(R.id.action_settingsFragment_to_teamInvitationsFragment)
            }
            txtAttendTournaments.setOnClickListener {
                //Katıldığım Turnuvalar
                UserModel.instance.profilePageItem=2
                navigate(R.id.profileFragment)
            }
            txtMyAchievements.setOnClickListener {
                //Başarılarım
                UserModel.instance.profilePageItem=1
                navigate(R.id.profileFragment)
            }
            txtMyScreenshots.setOnClickListener {
                //Ekran Görüntülerim
                navigate(R.id.action_settingsFragment_to_screenshotsFragment)
            }
            icBack.setOnClickListener { popBackStack() }

            support.setOnClickListener {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://discord.com/invite/nerf-it-1023937033196023819")
                startActivity(openURL)
            }

            logout.setOnClickListener {
                showQuestionDialog(getString(R.string.logout_question)) {
                    viewModel.clearUserInfo()
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
        }


        viewModel.user.fetchData()
    }


}