package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.refereechat

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournamentrefereemessages.TournamentRefereeUserChatItem
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.DialogSelectFileBinding
import com.nuevo.gameness.databinding.FragmentRefereeChatBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.*
import com.nuevo.gameness.utils.Constants.WEB_SOCKET_REFEREE_MESSAGE_CHAT
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class RefereeChatFragment : BaseFragment<FragmentRefereeChatBinding>() {

    private val viewModel by viewModels<RefereeChatViewModel>()
    private val refereeChatAdapter = RefereeChatAdapter()
    private var logoUploadDialog: Dialog? = null
    private var logoUploadDialogBinding: DialogSelectFileBinding? = null
    private var isImageChooseFromFiles = false
    private var checkPermissions: CheckPermissions? = null
    private var isMessageBottom = true
    private var webSocket: WebSocket? = null


    override fun getViewBinding() = FragmentRefereeChatBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = false)
        setBackButton(false)

        viewModel.tournamentId = arguments?.getString("id")

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

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
        viewModel.getTournamentRefereeUserChatList.fetchData()
        viewModel.setRefereeMessageAsRead.fetchData()

        viewModel.getTournamentRefereeUserChatList.state.observe(this) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        refereeChatAdapter.updateAllData(items)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.setRefereeMessageAsRead.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {}
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.sendRefereeMessage.state.observe(this) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { item ->
                            refereeChatAdapter.addData(0, item)
                            binding.editTextInputMessage.text.clear()
                            setBottomPositionRecyclerView()
                        }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {
                    state.show()
                    if (state.code == 429) showToast(getString(R.string.send_message_warning))
                }
            }
        }
    }



    private fun events() {
        binding.apply {
            recyclerViewRefereeChat.rotation = 180F
            recyclerViewRefereeChat.adapter = refereeChatAdapter

            val suggestions =resources.getStringArray(R.array.chat_suggestions)





            for (s in suggestions) {

                val tv = TextView(context)
                tv.text = s
                val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.setMargins(20.dp)
                context?.let {
                    tv.setTextColor(ContextCompat.getColor(it, R.color.blue));
                }
                tv.layoutParams = lp
                lnSuggestions.addView(tv)
                tv.setOnClickListener {
                    editTextInputMessage.setText(s)
                    layoutSend.setMyBackgroundTintList(context, R.color.blue)
                    editTextInputMessage.requestFocus()
                    editTextInputMessage.setSelection(editTextInputMessage.length())
                }

            }

            imageViewSuggestionsCollapse.setOnClickListener {

                if(scvSuggestions.visibility == View.VISIBLE) {
                    scvSuggestions.visibility = View.GONE
                } else {
                    scvSuggestions.visibility = View.VISIBLE
                }

            }

            webSocket = openWebSocketListener(
                WEB_SOCKET_REFEREE_MESSAGE_CHAT + UserModel.instance.token,
                "ReceiveMessage"
            ) { response ->
                if (!response.arguments.isNullOrEmpty())
                    if (viewModel.tournamentId == response.arguments[0]) {
                        refereeChatAdapter.addData(0, TournamentRefereeUserChatItem(
                            message = response.arguments[1],
                            isAdminResponse = true,
                            messageType = response.arguments[2].toInt()
                        ))
                        setBottomPositionRecyclerView()
                    }
            }

            checkPermissions = object : CheckPermissions(this@RefereeChatFragment) {
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
                            val file = File(data.data.getFilePath(requireContext()).toString())
                            val requestBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                            requestBuilder.addFormDataPart("TournamentId", viewModel.tournamentId.toString())
                            requestBuilder.addFormDataPart(
                                "Image", file.name, file
                                    .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                            )
                            viewModel.requestBody = requestBuilder.build()
                            viewModel.sendRefereeMessage.fetchData()
                        }
                    }
                }
            }

            editTextInputMessage.addTextChangedListener { input ->
                val text = (input ?: "").trim()
                when(text.length) {
                    0 -> layoutSend.setMyBackgroundTintList(context, R.color.tertiary_dark)
                    1 -> layoutSend.setMyBackgroundTintList(context, R.color.blue)
                }
            }

            layoutSend.setOnClickListener {
                val message = editTextInputMessage.text.trim().toString()
                sendMessage(message)
            }

            imageViewClose.setOnClickListener {
                popBackStack()
            }

            imageViewChooseFromImages.setOnClickListener {
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

            recyclerViewRefereeChat.setOnScrollChangeListener { _, _, _, _, _ ->
               if (refereeChatAdapter.itemCount > 0) {
                   val verticalScrollOffset = recyclerViewRefereeChat.computeVerticalScrollOffset()
                   val lastItemHeight = recyclerViewRefereeChat.getChildAt(0).measuredHeight
                   isMessageBottom = verticalScrollOffset < lastItemHeight
               }
            }
        }
    }

    private fun sendMessage(message: String) {
        binding.let {
            if (message.isNotEmpty()) {
                val requestBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBuilder.addFormDataPart("TournamentId", viewModel.tournamentId.toString())
                requestBuilder.addFormDataPart("Message", message)
                viewModel.requestBody = requestBuilder.build()
                viewModel.sendRefereeMessage.fetchData()
            }
        }

    }

    private fun setBottomPositionRecyclerView() {
        if (isMessageBottom) {
            binding.recyclerViewRefereeChat.postDelayed({
                binding.recyclerViewRefereeChat.smoothScrollToPosition(0)
            }, 222)
        }
    }

    override fun onDestroyView() {
        webSocket?.close(1000, "RefereeChatFragment OnDestroyView")
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onDestroyView()
    }

}
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
