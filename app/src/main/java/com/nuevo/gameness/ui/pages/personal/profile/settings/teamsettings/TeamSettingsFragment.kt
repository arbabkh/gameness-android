package com.nuevo.gameness.ui.pages.personal.profile.settings.teamsettings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.model.settings.TeamData
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTeamSettingsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameAdapter
import com.nuevo.gameness.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class TeamSettingsFragment: BaseFragment<FragmentTeamSettingsBinding>() {

    private val viewModel by viewModels<TeamSettingsViewModel>()
    private var teamInfo: TeamData? = null

    private val myTeamGamesAdapter = ChooseGameAdapter(0)
    private val allGameAdapter = ChooseGameAdapter(0)
    private val allGameList = arrayListOf<GameListItem>()

    private val pickImage = 1
    private var uri: Uri?=null

    private var onPermissionGranted: ((checkPermission: Boolean) -> Unit)? = null
    private val READ_EXTERNAL_STORAGE = 999
    private  var filename=""

    override fun getViewBinding() = FragmentTeamSettingsBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            recyclerMyTeamGames.adapter = myTeamGamesAdapter
            recyclerAllGames.adapter = allGameAdapter

            allGameAdapter.onItemClickListener = { _, item ->
                allGameList.find { item.id == it.id }?.selected = item.selected
                buttonColorChange()
            }
            myTeamGamesAdapter.onItemClickListener = { _, item ->
                allGameList.find { item.id == it.id }?.selected = item.selected
                buttonColorChange()
            }

            btnLoadImage.setOnClickListener {
                checkPermission()
            }

            btnSave.setOnClickListener {
                when {
                    uri == null -> showToast(getString(R.string.you_did_not_choose_logo))
                    edtNewTeamName.text.isNullOrEmpty() -> showToast(getString(R.string.you_must_enter_new_team_name))
                    else -> {
                        viewModel.request= loadFile(uri)
                        viewModel.changeTeamInformation.fetchData()
                    }
                }
            }
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData(){
        viewModel.myTeam.fetchData()
        viewModel.gameList.fetchData()

        viewModel.myTeam.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                       val success=state.data.success
                        if(success){
                            teamInfo=state.data.data
                            edtTeamName.setText(teamInfo!!.name)
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.myTeamGameList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        for (item in items) allGameList.find { item.id == it.id }?.selected = true
                        buttonColorChange()
                        myTeamGamesAdapter.updateAllData(allGameList.filter { it.selected })
                        allGameAdapter.updateAllData(allGameList.filter { !it.selected })
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        //tüm oyunlar - kendi seçtiğim oyunları çıkardım
        viewModel.gameList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        allGameList.clear()
                        allGameList.addAll(items)
                        viewModel.myTeamGameList.fetchData()
                        allGameAdapter.updateAllData(allGameList)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.changeTeamInformation.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                   showToast(getString(R.string.successful))
                    binding.edtNewTeamName.text.clear()
                    viewModel.myTeam.fetchData()
                    viewModel.gameList.fetchData()
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE)
        } else{
            onPermissionGranted?.invoke(true)
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    onPermissionGranted?.invoke(true)
                else onPermissionGranted?.invoke(false)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            if (data == null) {
                showToast(getString(R.string.file_could_not_be_selected))
                return
            }
            uri = data.data
            val imagePath = uri.getFilePath(requireContext())
            filename = imagePath!!.substringAfterLast("/")
            binding.txtTeamLogo.text = filename
        }
    }

    private fun loadFile(uri: Uri?): RequestBody {
        val imagePath = uri.getFilePath(requireContext()).toString()

        val bitmap = BitmapFactory.decodeFile(imagePath)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val requestBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("NewTeamName", binding.edtNewTeamName.text.toString())
            .addFormDataPart(
                "NewLogo",
                filename,
                File(imagePath).asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        val gameList = allGameList.filter { it.selected }
        //kaç oyun seçili ise ekliyoruz
        for (element in gameList) {
            requestBuilder.addFormDataPart("SelectedGameIds", element.id.toString())
        }

        return requestBuilder.build()
    }

    private fun buttonColorChange() {
        binding.apply {
            if (allGameList.any { it.selected }) {
                btnSave.setMyBackgroundTintList(context, R.color.blue)
                btnSave.setMyTextColor(context, R.color.white)
            } else {
                btnSave.setMyBackgroundTintList(context, R.color.tertiary_dark)
                btnSave.setMyTextColor(context, R.color.gray)
            }
        }
    }

}