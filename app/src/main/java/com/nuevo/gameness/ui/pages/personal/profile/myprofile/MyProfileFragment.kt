package com.nuevo.gameness.ui.pages.personal.profile.myprofile

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.DialogSelectFileBinding
import com.nuevo.gameness.databinding.FragmentMyProfileBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.ProfileTablayoutAdapter
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.achievements.AchievementsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.attendtournaments.AttendTournamentsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myprofile.discover.DiscoverFragment
import com.nuevo.gameness.utils.CheckPermissions
import com.nuevo.gameness.utils.getFilePath
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    private val viewModel by viewModels<MyProfileViewModel>()
    private lateinit var profileAdapter: ProfileTablayoutAdapter


    // upload profile
    private var profilePictureUploadDialog: Dialog? = null
    private var logoUploadDialogBinding: DialogSelectFileBinding? = null
    private var isImageChooseFromFiles = false
    private var checkPermissions: CheckPermissions? = null

    override fun getViewBinding() = FragmentMyProfileBinding.inflate(layoutInflater)

    private fun initUploadProfilePictureDialog() {

        profilePictureUploadDialog = Dialog(requireContext()).apply {
            logoUploadDialogBinding = DialogSelectFileBinding.inflate(layoutInflater)
            setCancelable(true)
            setContentView(logoUploadDialogBinding!!.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.BOTTOM)
        }

        logoUploadDialogBinding?.textViewCancel?.setOnClickListener {
            profilePictureUploadDialog?.cancel()
        }
        logoUploadDialogBinding?.textViewChooseFromPhotos?.setOnClickListener {
            isImageChooseFromFiles = false
            checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        logoUploadDialogBinding?.textViewChooseFromFiles?.setOnClickListener {
            isImageChooseFromFiles = true
            checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }
    override fun initView() {


        initUploadProfilePictureDialog()

        checkPermissions = object : CheckPermissions(this@MyProfileFragment) {
            override fun permissionResultSuccess(permission: String) {
                when(permission) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        val data = Intent()
                        data.type = "image/*"
                        data.action =
                            if (isImageChooseFromFiles) Intent.ACTION_GET_CONTENT
                            else Intent.ACTION_PICK
                        intentResultLaunch(data, permission)
                    }
                }
            }
            override fun permissionResultDontAsk(permission: String) {
                showDialog("You need to go to the application settings and give Read Storage Permission to continue to upload your picture")
            }
            override fun intentResult(data: Intent, requestPermission: String) {
                super.intentResult(data, requestPermission)
                profilePictureUploadDialog?.cancel()
                when(requestPermission) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        data.data.getFilePath(requireContext())?.let { path ->
                            viewModel.fileForRequestBody = File(path)
                            viewModel.requestBody.addFormDataPart(
                                "Image", viewModel.fileForRequestBody?.name, viewModel.fileForRequestBody!!
                                    .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                            )

                            viewModel.changeProfilePicture.fetchData()

                        }
                    }
                }
            }
        }


        profileAdapter= ProfileTablayoutAdapter(childFragmentManager)
        profileAdapter.addFragment(DiscoverFragment(), getString(R.string.discover))
        profileAdapter.addFragment(AchievementsFragment(), getString(R.string.tournament_achievements))
        profileAdapter.addFragment(AttendTournamentsFragment(), getString(R.string.tournaments_attended))

        //Adapter'ımızdaki verileri viewPager adapter'a veriyoruz
        binding.viewPagerProfile.adapter = profileAdapter
        binding.viewPagerProfile.currentItem = UserModel.instance.profilePageItem
        //Tablar arasında yani viewPager'lar arasında geçisi sağlıyoruz
        binding.tabs.setupWithViewPager(binding.viewPagerProfile)

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                UserModel.instance.profilePageItem=0
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                UserModel.instance.profilePageItem=1
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                UserModel.instance.profilePageItem=2
            }
        })


        binding.imgProfile.setOnClickListener {
            profilePictureUploadDialog?.show()
        }

        fetchData()
    }

    private fun fetchData() {
        viewModel.getUser.fetchData()
        viewModel.myTeam.fetchData()

        viewModel.getUser.state.observe(viewLifecycleOwner){ state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data?.let { item ->
                            txtName.text = item.userName
                            //takım kaptanı değilse title gözükmeyecek şimdilik
                            if(item.isTeamLeader){
                                txtTeamTitle.visibility= View.VISIBLE
                                UserModel.instance.isTeamLeader=true
                            }else{
                                txtTeamTitle.visibility=View.GONE
                            }
                            item.imageUrl?.let { url -> imgProfile.load(requireContext(), url) }
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        //burdan takım logosunu ve adını alıcaz
        viewModel.myTeam.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val success=state.data.success
                        if(success){
                            val teamInfo=state.data.data
                            val url=teamInfo.logoURL
                            Glide.with(requireContext()).load(url).into(imgTeamLogo)
                            txtTeamName.text=teamInfo.name
                        }else{
                            imgTeamLogo.visibility=View.GONE
                            txtTeamName.visibility=View.GONE
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.changeProfilePicture.state.observe(viewLifecycleOwner){ state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data?.let { item ->
                            txtName.text = item.userName
                            //takım kaptanı değilse title gözükmeyecek şimdilik
                            if(item.isTeamLeader){
                                txtTeamTitle.visibility= View.VISIBLE
                                UserModel.instance.isTeamLeader=true
                            }else{
                                txtTeamTitle.visibility=View.GONE
                            }
                            item.imageUrl?.let { url -> imgProfile.load(requireContext(), url) }
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

    }

}