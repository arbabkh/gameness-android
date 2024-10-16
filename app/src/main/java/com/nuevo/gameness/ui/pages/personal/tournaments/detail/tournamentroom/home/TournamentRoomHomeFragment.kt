package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home

import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentData
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentRoomHomeBinding
import com.nuevo.gameness.databinding.LayoutCountDownBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.Constants.WEB_SOCKET_READY_MESSAGE_CHAT
import com.nuevo.gameness.utils.Constants.WEB_SOCKET_REFEREE_MESSAGE_CHAT
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.openWebSocketListener
import com.nuevo.gameness.utils.toCalendar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.WebSocket
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TournamentRoomHomeFragment : BaseFragment<FragmentTournamentRoomHomeBinding>() {

    private val viewModel by viewModels<TournamentRoomHomeViewModel>()
    private var cdt: CountDownTimer? = null
    private var tournamentData: TournamentData? = null
    private lateinit var tournamentRoomHomeAdapter: TournamentRoomHomeAdapter
    private lateinit var webSocketRefereeNewMessage: WebSocket
    private lateinit var webSocketReadyNewMessage: WebSocket

    override fun getViewBinding() = FragmentTournamentRoomHomeBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(true) { popBackStack() }

        viewModel.tournamentId = requireArguments().getString("id")

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.checkNewRefereeMessage.fetchData()
        viewModel.checkNewReadyMessage.fetchData()
        viewModel.getTournament.fetchData()
        viewModel.getNextMatch.fetchData()

        viewModel.getNextMatch.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        if (state.data.success == true) {
                            imageViewChat.visibility = View.VISIBLE

                            state.data.data?.let { item ->
                                imageViewTournamentYourNextMatchHome.load(requireContext(), item.homeLogoImageUrl)
                                imageViewTournamentYourNextMatchAway.load(requireContext(), item.awayLogoUrl)
                                textViewTournamentYourNextMatchHome.text = item.homeName
                                textViewTournamentYourNextMatchAway.text = item.awayName

                                layoutCountDown.apply {
                                    item.nextMatchDate?.toCalendar()?.time?.time?.let { time ->
                                        val diff = time - Calendar.getInstance().time.time

                                        if (diff > 1000) {
                                            cdt = object : CountDownTimer(diff, 1000) {
                                                override fun onTick(millisUntilFinished: Long) {
                                                    var millisUntilFinishedTemp = millisUntilFinished
                                                    val days: Long = TimeUnit.MILLISECONDS.toDays(
                                                        millisUntilFinishedTemp
                                                    )
                                                    millisUntilFinishedTemp -= TimeUnit.DAYS.toMillis(
                                                        days
                                                    )

                                                    val hours: Long = TimeUnit.MILLISECONDS.toHours(
                                                        millisUntilFinishedTemp
                                                    )
                                                    millisUntilFinishedTemp -= TimeUnit.HOURS.toMillis(
                                                        hours
                                                    )

                                                    val minutes: Long =
                                                        TimeUnit.MILLISECONDS.toMinutes(
                                                            millisUntilFinishedTemp
                                                        )
                                                    millisUntilFinishedTemp -= TimeUnit.MINUTES.toMinutes(
                                                        minutes
                                                    )

                                                    var seconds: Long =
                                                        TimeUnit.MILLISECONDS.toSeconds(
                                                            millisUntilFinishedTemp
                                                        )

                                                    millisUntilFinishedTemp -= TimeUnit.SECONDS.toSeconds(
                                                        seconds
                                                    )

                                                 /*   val seconds = millisUntilFinished / 1000
                                                    val minutes = seconds / 60
                                                    val hours = minutes / 60
                                                    val days = TimeUnit.MILLISECONDS.toDays(
                                                        millisUntilFinished
                                                    )*/

                                                    seconds = millisUntilFinishedTemp / 1000 % 60;


                                                    var temp = if (days > 9) days.toString() else "0$days"
                                                    textViewCountDownDayNumber.text = temp
                                                    temp = (if (days == 0L) hours else (hours % days)).let { if (it > 9) it else "0$it" }.toString()
                                                    textViewCountDownHourNumber.text = temp
                                                    temp = (if (hours == 0L) minutes else (minutes % hours)).let { if (it > 9) it else "0$it" }.toString()
                                                    textViewCountDownMinuteNumber.text = minutes.toString()
                                                    temp = (if (minutes == 0L) seconds else (seconds % minutes)).let { if (it > 9) it else "0$it" }.toString()
                                                    textViewCountDownSecondNumber.text = seconds.toString();

                                                }

                                                override fun onFinish() {}
                                            }
                                            cdt?.start()
                                        } else {
                                            disableCountDown(layoutCountDown);
                                            layoutAvailabilityMessage.textViewAvailabilityStatus.visibility = View.VISIBLE;
                                            layoutAvailabilityMessage.textViewAvailabilityStatus.text = getTournamentStatusText();
                                        }
                                    }
                                }
                            }
                        } else {
                            imageViewChat.visibility = View.GONE;
                            disableCountDown(layoutCountDown);
                            layoutAvailabilityMessage.textViewAvailabilityStatus.visibility = View.VISIBLE;
                            layoutAvailabilityMessage.textViewAvailabilityStatus.text = getTournamentStatusText();
                            //showToast(state.data.message.toString())
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {

                is NetworkResult.Success -> {
                    if (state.data.success == true) {

                        state.data.data?.let { item ->
                            tournamentData = item
                            binding.apply {
                                imageViewTournamentRoomHome.load(requireContext(), item.detailImageUrl)
                                imageViewTournamentRoomHomePlatform.load(requireContext(), item.platformImageUrl)

                                tournamentRoomHomeAdapter = TournamentRoomHomeAdapter(
                                    this@TournamentRoomHomeFragment,
                                    requireArguments().apply { putInt(
                                        "participation_type", item.tournamentParticipationType ?: -1
                                    ) },
                                    item.tournamentType
                                )

                                layoutAvailabilityMessage.apply {
                                    when (item.tournamentAvailability) {
                                        4 -> {
                                            textViewAvailabilityStatus.visibility = View.VISIBLE;
                                            textViewAvailabilityStatus.text = resources.getString(R.string.tournament_ended);
                                            disableCountDown(layoutCountDown);
                                        }
                                       3 -> {
                                           enableCountdown(layoutCountDown);
                                           textViewAvailabilityStatus.visibility = View.INVISIBLE;
                                        }
                                        2 -> {
                                            enableCountdown(layoutCountDown);
                                            textViewAvailabilityStatus.visibility = View.INVISIBLE;
                                        }
                                        1 -> {
                                            disableCountDown(layoutCountDown);
                                            textViewAvailabilityStatus.visibility = View.VISIBLE;
                                            textViewAvailabilityStatus.text = resources.getString(R.string.registration_period);
                                        }
                                        0 -> {
                                            disableCountDown(layoutCountDown);
                                            textViewAvailabilityStatus.visibility = View.VISIBLE;
                                            textViewAvailabilityStatus.text = resources.getString(R.string.announced);
                                        }
                                    }
                                }

                                viewPager2TournamentRoomHome.adapter = tournamentRoomHomeAdapter

                                TabLayoutMediator(tabLayoutTournamentRoomHome, viewPager2TournamentRoomHome) { tab, position ->
                                    tab.text = when(position) {
                                        3 -> getString(R.string.scoreboard)
                                        2 -> getString(R.string.fixture)
                                        1 -> getString(R.string.players)
                                        else -> getString(R.string.general_information)
                                    }
                                }.attach()
                                viewPager2TournamentRoomHome.offscreenPageLimit = 3
                                if (requireArguments().getString("user_id") != null)
                                    viewPager2TournamentRoomHome.currentItem = 1
                            }
                        }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.checkNewRefereeMessage.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    val resId =
                        if (state.data.success == true) R.color.blue
                        else R.color.white
                    binding.imageViewRefereeChat.imageTintList = ContextCompat.getColorStateList(requireContext(), resId)
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.checkNewReadyMessage.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    val resId =
                        if (state.data.success == true) R.color.blue
                        else R.color.white
                    binding.imageViewChat.imageTintList = ContextCompat.getColorStateList(requireContext(), resId)
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun disableCountDown(countDownBinding: LayoutCountDownBinding){
        countDownBinding.textViewCountDownDayNumber.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownHourNumber.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownMinuteNumber.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownSecondNumber.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownDayText.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownHourText.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownMinuteText.visibility = View.INVISIBLE;
        countDownBinding.textViewCountDownSecondText.visibility = View.INVISIBLE;
        countDownBinding.textViewDots1.visibility = View.INVISIBLE;
        countDownBinding.textViewDots2.visibility = View.INVISIBLE;
        countDownBinding.textViewDots3.visibility = View.INVISIBLE;
    }

    private fun enableCountdown(countDownBinding: LayoutCountDownBinding){
        countDownBinding.textViewCountDownDayNumber.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownHourNumber.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownMinuteNumber.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownSecondNumber.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownDayText.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownHourText.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownMinuteText.visibility = View.VISIBLE;
        countDownBinding.textViewCountDownSecondText.visibility = View.VISIBLE;
        countDownBinding.textViewDots1.visibility = View.VISIBLE;
        countDownBinding.textViewDots2.visibility = View.VISIBLE;
        countDownBinding.textViewDots3.visibility = View.VISIBLE;
    }

    private fun getTournamentStatusText(): String {
        var tournamentStatus = "";
        when (tournamentData?.tournamentAvailability) {
            4 -> {
                tournamentStatus = resources.getString(R.string.tournament_ended);
            }
            3 -> {
                tournamentStatus = resources.getString(R.string.match_period);
            }
            2 -> {
                tournamentStatus = resources.getString(R.string.registration_period_ended);
            }
            1 -> {
                tournamentStatus = resources.getString(R.string.registration_period);
            }
            0 -> {
                tournamentStatus = resources.getString(R.string.announced);
            }
        }

        return tournamentStatus;
    }

    private fun events() {
        binding.apply {
            webSocketRefereeNewMessage = openWebSocketListener(
                WEB_SOCKET_REFEREE_MESSAGE_CHAT + viewModel.getToken(),
                "ReceiveNewMessage"
            ) { response ->
                if (!response.arguments.isNullOrEmpty())
                    if (viewModel.tournamentId == response.arguments[0])
                        imageViewRefereeChat.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }
            webSocketReadyNewMessage = openWebSocketListener(
                WEB_SOCKET_READY_MESSAGE_CHAT + viewModel.getToken(),
                "ReceiveNewMessage"
            ) { response ->
                if (!response.arguments.isNullOrEmpty())
                    if (viewModel.tournamentId == response.arguments[0])
                        imageViewChat.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }

            imageViewRefereeChat.setOnClickListener {
                navigate(
                    R.id.action_tournamentRoomHomeFragment_to_refereeChatFragment,
                    bundleOf("id" to viewModel.tournamentId)
                )
            }
            imageViewChat.setOnClickListener {
                if (tournamentData?.tournamentParticipationType == 1) { // Bireysel turnuva
                    tournamentRoomHomeAdapter.playersFragment.loadMessageBox()
                } else if (tournamentData?.tournamentParticipationType == 2) { // Takım turnuvası
                    navigate(
                        R.id.action_tournamentRoomHomeFragment_to_playerChatFragment,
                        bundleOf("id" to tournamentData?.id)
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        webSocketRefereeNewMessage.close(1000, "TournamentRoomHomeFragment OnDestroyView")
        webSocketReadyNewMessage.close(1000, "TournamentRoomHomeFragment OnDestroyView")
        cdt?.cancel()
        cdt = null
        super.onDestroyView()
    }

}