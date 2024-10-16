package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.readymessages.MessageHistoryItem
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.DialogMessageBinding
import com.nuevo.gameness.databinding.FragmentPlayerChatBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter.PlayerChatAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter.MessageHistoryAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter.ReadyMessageAdapter
import com.nuevo.gameness.utils.Constants.WEB_SOCKET_READY_MESSAGE_CHAT
import com.nuevo.gameness.utils.openWebSocketListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket

@AndroidEntryPoint
class PlayerChatFragment : BaseFragment<FragmentPlayerChatBinding>() {

    private val viewModel by viewModels<PlayerChatViewModel>()
    private val playerChatAdapter = PlayerChatAdapter()
    private val readyMessageAdapter = ReadyMessageAdapter()
    private val messageHistoryAdapter = MessageHistoryAdapter()
    private var isMessageBottom = true
    private var dialogMessageBinding: DialogMessageBinding? = null
    private lateinit var webSocket: WebSocket
    private lateinit var dialogMessage: Dialog

    override fun getViewBinding() = FragmentPlayerChatBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.tournamentID = arguments?.getString("id")

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getReadyMessageInbox.fetchData()

        viewModel.getReadyMessageInbox.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { items ->
                            playerChatAdapter.updateAllData(items)
                        }
                    } else showToast(getString(R.string.no_message))
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getReadyMessageList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        readyMessageAdapter.updateAllData(items)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getReadyMessageHistoryList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    showMessageDialog()
                    state.data.data?.let { item ->
                        messageHistoryAdapter.updateAllData(item.reversed())
                    }
                    viewModel.getReadyMessageList.fetchData()
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.sendReadyMessage.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.data?.let { item ->
                        messageHistoryAdapter.addData(0, MessageHistoryItem(
                            message = item.name,
                            senderImageURL = item.senderImageUrl,
                            senderID = UserModel.instance.id
                        ))
                        setBottomPositionRecyclerView()
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> {
                    state.show()
                    if (state.code == 429) showToast(getString(R.string.send_message_warning))
                }
            }
        }
        viewModel.setReadyMessageAsRead.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {}
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {
            recyclerViewPlayerChat.adapter = playerChatAdapter

            webSocket = openWebSocketListener(
                WEB_SOCKET_READY_MESSAGE_CHAT + viewModel.getToken(),
                "ReceiveMessage"
            ) { response ->
                if (dialogMessage.isShowing)
                    if (!response.arguments.isNullOrEmpty())
                        if (response.arguments[0] == viewModel.recipientID)
                            if (response.arguments[1] == viewModel.tournamentID) {
                                messageHistoryAdapter.addData(0, MessageHistoryItem(
                                    message = response.arguments[2],
                                    recipientImageURL = response.arguments[3],
                                    senderID = viewModel.recipientID
                                ))
                                setBottomPositionRecyclerView()
                                viewModel.setReadyMessageAsRead.fetchData()
                            }
            }

            playerChatAdapter.onItemClickListener = { _, item ->
                viewModel.recipientID = item.chatUserId
                viewModel.getReadyMessageHistoryList.fetchData()
                if (item.isReaded == false) viewModel.setReadyMessageAsRead.fetchData()
            }

            readyMessageAdapter.onItemClickListener = { _, item ->
                viewModel.readyMessageID = item.id
                viewModel.sendReadyMessage.fetchData()
            }
        }
    }

    private fun showMessageDialog() {
        if (!(::dialogMessage.isInitialized)) {
            dialogMessage = Dialog(requireContext())
            dialogMessageBinding = DialogMessageBinding.inflate(dialogMessage.layoutInflater)

            dialogMessageBinding!!.recyclerViewMessageHistory.rotation = 180F
            dialogMessageBinding!!.recyclerViewReadyMessages.adapter = readyMessageAdapter
            dialogMessageBinding!!.recyclerViewMessageHistory.adapter = messageHistoryAdapter

            dialogMessageBinding!!.apply {
                recyclerViewMessageHistory.setOnScrollChangeListener { _, _, _, _, _ ->
                    if (messageHistoryAdapter.itemCount > 0) {
                        val verticalScrollOffset = recyclerViewMessageHistory.computeVerticalScrollOffset()
                        val lastItemHeight = recyclerViewMessageHistory.getChildAt(0).measuredHeight
                        isMessageBottom = verticalScrollOffset < lastItemHeight
                    }
                }
            }

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            dialogMessageBinding!!.layoutMessage.layoutParams.width = width * 9 / 10
            dialogMessageBinding!!.layoutMessage.layoutParams.height = height * 3 / 5

            dialogMessageBinding!!.imageViewClose.setOnClickListener {
                dialogMessage.cancel()
            }

            dialogMessage.setOnCancelListener {
                viewModel.getReadyMessageInbox.fetchData()
            }

            dialogMessage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogMessage.setCancelable(true)
            dialogMessage.setContentView(dialogMessageBinding!!.root)
        }
        dialogMessage.show()
    }

    private fun setBottomPositionRecyclerView() {
        dialogMessageBinding?.apply {
            if (isMessageBottom) {
                recyclerViewMessageHistory.postDelayed({
                    recyclerViewMessageHistory.smoothScrollToPosition(0)
                }, 222)
            }
        }
    }

    override fun onDestroyView() {
        webSocket.close(1000, "PlayerChatFragment OnDestroyView")
        super.onDestroyView()
    }

}