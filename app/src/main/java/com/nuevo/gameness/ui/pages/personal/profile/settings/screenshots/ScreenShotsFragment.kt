package com.nuevo.gameness.ui.pages.personal.profile.settings.screenshots

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.ScreenShotItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.DialogSelectFileBinding
import com.nuevo.gameness.databinding.FragmentScreenshotsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.CheckPermissions
import com.nuevo.gameness.utils.getFilePath
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class ScreenShotsFragment : BaseFragment<FragmentScreenshotsBinding>() {


    private val viewModel by viewModels<ScreenShotsViewModel>()
    private lateinit var matchesAdapter: ArrayAdapter<ScreenShotItem>
    private var matchList: ArrayList<ScreenShotItem> = arrayListOf()
    private var logoUploadDialog: Dialog? = null
    private var logoUploadDialogBinding: DialogSelectFileBinding? = null
    private var isImageChooseFromFiles = false
    private var checkPermissions: CheckPermissions? = null

    override fun getViewBinding() = FragmentScreenshotsBinding.inflate(layoutInflater)

    override fun initView() {

        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)
        logoUploadDialog = Dialog(requireContext()).apply {
            logoUploadDialogBinding = DialogSelectFileBinding.inflate(layoutInflater)
            setCancelable(true)
            setContentView(logoUploadDialogBinding!!.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.BOTTOM)
        }
        binding.run {
            matchesSpinner.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        matchesSpinner.dropDownVerticalOffset =
                            matchesSpinner.dropDownVerticalOffset + matchesSpinner.height
                        matchesSpinner.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })
        }
        events()
        fetchData()

    }

    private fun events() {
        checkPermissions = object : CheckPermissions(this@ScreenShotsFragment) {
            override fun permissionResultSuccess(permission: String) {
                when (permission) {
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
                when (requestPermission) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        data.data.getFilePath(requireContext())?.let { path ->
                            viewModel.fileForRequestBody = File(path)

                            val myBitmap = BitmapFactory.decodeFile(path)
                            binding.screenShotImage.setImageBitmap(myBitmap)
                            binding.screenShotImage.visibility = View.VISIBLE
                            binding.btnSave.text = getString(R.string.load)
                            binding.btnSave.visibility = View.VISIBLE

                        }
                    }
                }
            }
        }
        binding.apply {
            icBack.setOnClickListener { popBackStack() }
            matchesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                   populateLayout()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            btnSave.setOnClickListener {
                if (btnSave.text == getString(R.string.choose_from_photos)) {
                    logoUploadDialog?.show()
                } else if (btnSave.text == getString(R.string.load)) {
                    if (viewModel.fileForRequestBody != null) {
                        Log.d("ozge","setOnClickListener matchesSpinner.selectedItem: " +   binding.matchesSpinner.selectedItem?.toString())

                        viewModel.selected = binding.matchesSpinner.selectedItem as? ScreenShotItem

                        Log.d("ozge","setOnClickListener selected:  " + viewModel.selected?.toString())


                        viewModel.requestBody.addFormDataPart(
                            "MatchId",
                            (matchesSpinner.selectedItem as? ScreenShotItem)?.matchId.toString()
                        )
                        viewModel.requestBody.addFormDataPart(
                            "File",
                            viewModel.fileForRequestBody?.name,
                            viewModel.fileForRequestBody!!
                                .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                        )
                        viewModel.requestBody.addFormDataPart(
                            "TournamentId",
                            (matchesSpinner.selectedItem as? ScreenShotItem)?.tournamentId.toString()
                        )
                        viewModel.uploadScreenshots.fetchData()
                    }
                }

            }
            logoUploadDialogBinding?.textViewCancel?.setOnClickListener {
                logoUploadDialog?.cancel()
            }
            logoUploadDialogBinding?.textViewChooseFromPhotos?.setOnClickListener {
                isImageChooseFromFiles = false
            /*    when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected, and what
                    // features are disabled if it's declined. In this UI, include a
                    // "cancel" or "no thanks" button that lets the user continue
                    // using your app without granting the permission.
                        showDialog("You need to go to the application settings and give Read Storage Permission to continue to upload your screenshots")

                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }*/
             //   activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            logoUploadDialogBinding?.textViewChooseFromFiles?.setOnClickListener {
                isImageChooseFromFiles = true
                checkPermissions?.permissionResultLaunch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun populateLayout(){
        if ((binding.matchesSpinner.selectedItem as? ScreenShotItem)?.imageUrl != null) {
            loadAndShowScreenShotLayout()
        } else {
            loadUploadLayout()
        }
    }
    private fun loadUploadLayout() {
        binding.screenShotImage.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE
    }

    private fun loadAndShowScreenShotLayout() {
        binding.screenShotImage.load(
            requireContext(),
            (binding.matchesSpinner.selectedItem as? ScreenShotItem)?.imageUrl
        )
        binding.screenShotImage.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE
    }

    private fun fetchData() {

        viewModel.getScreenShots.fetchData()
        viewModel.getScreenShots.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->

                        matchList.clear()
                        matchList.addAll(items)

                        matchesAdapter =
                            ArrayAdapter(requireContext(), R.layout.item_category, matchList)
                        binding.matchesSpinner.adapter = matchesAdapter
                        if(viewModel.selected!=null){

                            for (item in matchList) {
                                if(item.matchId.equals(viewModel.selected!!.matchId)){
                                    val position = matchesAdapter.getPosition(item)
                                    binding.matchesSpinner.setSelection(position)
                                    matchesAdapter.notifyDataSetChanged()
                                    binding.screenShotImage.visibility = View.VISIBLE
                                    binding.btnSave.visibility = View.GONE
                                    break
                                }
                            }
                        /*    Log.d("ozge",viewModel.selected?.fileId?:"")
                            val position = matchesAdapter.getPosition(viewModel.selected)
                            Log.d("ozge","position: " + position )*/

                        /*    binding.matchesSpinner.setSelection(position)
                            matchesAdapter.notifyDataSetChanged()*/

                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.uploadScreenshots.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.success?.let {
                       if(it){
                          // viewModel.selected = binding.matchesSpinner.selectedItem as? ScreenShotItem
                         /*  Log.d("ozge","selected: " + viewModel.selected?.fileId)
                           Log.d("ozge","matchesSpinner.selectedItem: " +   binding.matchesSpinner.selectedItem?.toString())
*/                           viewModel.getScreenShots.fetchData()
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
