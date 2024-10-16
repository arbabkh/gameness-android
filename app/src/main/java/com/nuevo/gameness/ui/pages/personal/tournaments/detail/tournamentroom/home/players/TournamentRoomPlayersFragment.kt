package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.players

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
import com.nuevo.gameness.databinding.FragmentTournamentRoomPlayersBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter.MessageHistoryAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat.adapter.ReadyMessageAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.players.adapter.TournamentRoomTeamAdapter
import com.nuevo.gameness.utils.Constants.WEB_SOCKET_READY_MESSAGE_CHAT
import com.nuevo.gameness.utils.openWebSocketListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket

@AndroidEntryPoint
class TournamentRoomPlayersFragment : BaseFragment<FragmentTournamentRoomPlayersBinding>(){

    override fun getViewBinding() = FragmentTournamentRoomPlayersBinding.inflate(layoutInflater)

    private val viewModel by viewModels<TournamentRoomPlayersViewModel>()
    private val tournamentRoomTeamAdapter = TournamentRoomTeamAdapter()
    private val readyMessageAdapter = ReadyMessageAdapter()
    private val messageHistoryAdapter = MessageHistoryAdapter()
    private var isMessageBottom = true
    private var dialogMessageBinding: DialogMessageBinding? = null
    private lateinit var dialogMessage: Dialog
    private lateinit var webSocket: WebSocket

    override fun initView() {
        viewModel.tournamentID = requireArguments().getString("id")
        requireArguments().getString("user_id")?.let { userId ->
            viewModel.recipientID = userId
            viewModel.getReadyMessageHistoryList.fetchData()
        }

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getNextMatchTeamUsers.fetchData()

        viewModel.getNextMatchTeamUsers.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { nextMatchTeams ->
                            val homeTeamUsers = nextMatchTeams.homeTeam?.teamUsers
                            val awayTeamUsers = nextMatchTeams.awayTeam?.teamUsers
                            if (!homeTeamUsers.isNullOrEmpty() && !awayTeamUsers.isNullOrEmpty()) {
                                tournamentRoomTeamAdapter.updateAllData(arrayListOf(
                                    nextMatchTeams.homeTeam, nextMatchTeams.awayTeam
                                ))
                                if (requireArguments().getInt("participation_type") == 1) {
                                    /**
                                     * Bireysel turnuvada karşıda sadece 1 oyuncu olduğu için
                                     * kendi id'mizden farklı olan değeri aldık
                                     */
                                    viewModel.recipientID =
                                        if (homeTeamUsers[0].userID != UserModel.instance.id) homeTeamUsers[0].userID
                                        else awayTeamUsers[0].userID
                                }
                            }
                        }
                    } else showToast(state.data.message.toString())
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
                   if(state.data.success == true){
                       state.data.data?.let { items->
                           messageHistoryAdapter.updateAllData(items.reversed())
                       }
                       viewModel.getReadyMessageList.fetchData()
                   } else showToast(state.data.message.toString())
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
            recyclerViewTournamentRoomTeam.adapter = tournamentRoomTeamAdapter

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

            tournamentRoomTeamAdapter.onPlayerClickListener = { _, item ->
                if (item.userID != UserModel.instance.id) {
                    viewModel.recipientID = item.userID
                    viewModel.getReadyMessageHistoryList.fetchData()
                }
            }

            readyMessageAdapter.onItemClickListener = { _, item ->
                viewModel.readyMessageID = item.id
                viewModel.sendReadyMessage.fetchData()
            }
        }
    }

    fun loadMessageBox() {
        viewModel.getReadyMessageHistoryList.fetchData()
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
        webSocket.close(1000, "TournamentRoomPlayersFragment OnDestroyView")
        super.onDestroyView()
    }

}