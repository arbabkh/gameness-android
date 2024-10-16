package com.nuevo.gameness.ui.pages.personal.profile.myteam.createteam

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.DialogSelectFileBinding
import com.nuevo.gameness.databinding.FragmentCreateTeamBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameAdapter
import com.nuevo.gameness.utils.CheckPermissions
import com.nuevo.gameness.utils.getFilePath
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class CreateTeamFragment : BaseFragment<FragmentCreateTeamBinding>() {

    private val viewModel by viewModels<CreateTeamViewModel>()
    private val chooseGameAdapter = ChooseGameAdapter(0)
    private val chooseGameList = arrayListOf<GameListItem>()
    private var logoUploadDialog: Dialog? = null
    private var logoUploadDialogBinding: DialogSelectFileBinding? = null
    private var isImageChooseFromFiles = false
    private var checkPermissions: CheckPermissions? = null

    override fun getViewBinding() = FragmentCreateTeamBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = false)
        setBackButton(true){ popBackStack() }

        logoUploadDialog = Dialog(requireContext()).apply {
            logoUploadDialogBinding = DialogSelectFileBinding.inflate(layoutInflater)
            setCancelable(true)
            setContentView(logoUploadDialogBinding!!.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.BOTTOM)
        }

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getGameList.fetchData()

        viewModel.getGameList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        chooseGameList.clear()
                        chooseGameList.addAll(items)
                        chooseGameAdapter.updateAllData(chooseGameList)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.createTeam.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) popBackStack()
                    else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {

            checkPermissions = object : CheckPermissions(this@CreateTeamFragment) {
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

                    showDialog("You need to go to the application settings and give Read Storage Permission to continue to upload your screenshots")
                }
                override fun intentResult(data: Intent, requestPermission: String) {
                    super.intentResult(data, requestPermission)
                    logoUploadDialog?.cancel()
                    when(requestPermission) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            data.data.getFilePath(requireContext())?.let { path ->
                                viewModel.fileForRequestBody = File(path)
                                editTextCreateTeamLogoUpload.setText(viewModel.fileForRequestBody?.name)
                            }
                        }
                    }
                }
            }

            recyclerViewCreateTeamChooseGame.adapter = chooseGameAdapter

            buttonCreateTeam.setOnClickListener {
                val selectedChooseGameList = chooseGameList.filter { it.selected }
                if (viewModel.fileForRequestBody != null
                    && !editTextCreateTeamName.text.isNullOrEmpty()
                    && selectedChooseGameList.isNotEmpty()
                ) {
                    viewModel.requestBody.addFormDataPart("TeamName", editTextCreateTeamName.text.toString())
                    viewModel.requestBody.addFormDataPart(
                        "Logo", viewModel.fileForRequestBody?.name, viewModel.fileForRequestBody!!
                            .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                    )
                    for (item in selectedChooseGameList) {
                        viewModel.requestBody.addFormDataPart("SelectedGameIds", item.id.toString())
                    }
                    viewModel.createTeam.fetchData()
                }
            }
            buttonCreateTeamLogoUpload.setOnClickListener {
                logoUploadDialog?.show()
            }

            logoUploadDialogBinding?.textViewCancel?.setOnClickListener {
                logoUploadDialog?.cancel()
            }
            logoUploadDialogBinding?.textViewChooseFromPhotos?.setOnClickListener {
                isImageChooseFromFiles = false
                checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            logoUploadDialogBinding?.textViewChooseFromFiles?.setOnClickListener {
                isImageChooseFromFiles = true
                checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            chooseGameAdapter.onItemClickListener = { pos, item ->
                chooseGameList[pos].selected = item.selected
                chooseGameAdapter.updateAllData(chooseGameList)
            }
        }
    }

}