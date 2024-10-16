package com.nuevo.gameness.ui.pages.personal.tournaments

import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.data.model.tournaments.TournamentsStatusFilter
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.adapter.ButtonFilterAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.adapter.TournamentsAdapter
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.myContains
import com.nuevo.gameness.utils.showQuestionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentsFragment : BaseFragment<FragmentTournamentsBinding>() {

    override fun getViewBinding() = FragmentTournamentsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<TournamentsViewModel>()
    private val tournamentsAdapter = TournamentsAdapter()
    private val buttonFilterAdapter = ButtonFilterAdapter()
    private val tournamentList = arrayListOf<TournamentItem>()
    private lateinit var gamesCategoryAdapter: ArrayAdapter<String>
    private lateinit var buttonFilterList: ArrayList<TournamentsStatusFilter>
    private lateinit var gameList: ArrayList<String>
    private var navigateId: Int? = null


    private var handler: Handler? = null

    val delay: Long = Constants.AUTO_REFRESH_RATE.toLong() // Refresh every 30 seconds

    private val refreshTask = object: Runnable {
        override fun run() {
            reFetch()
            handler?.postDelayed(this, delay)
        }

    }





    override fun initView() {
        if (arguments?.containsKey("navigate_id") == true) {
            /**
             * nav_tournaments icerisine yonlendirme
             */
            if (navigateId == null) {
                navigateId = requireArguments().getInt("navigate_id")
                navigate(navigateId!!, requireArguments())
            } else popBackStack()
        } else {
            setBottomVisibility(bottomLine = true, bottomMenu = true)
            setBackButton(visible = false)

            buttonFilterList = arrayListOf(
                TournamentsStatusFilter(getString(R.string.all), 0, true),
                TournamentsStatusFilter(getString(R.string.applied), 1, false),
                TournamentsStatusFilter(getString(R.string.approved_recipients), 2, false)
            )
            buttonFilterAdapter.updateAllData(buttonFilterList)

            fetchData()
            events()
        }
    }

    private fun reFetch() {

        viewModel.getTournaments.fetchData()
        viewModel.getGameList.fetchData()

    }

    private fun fetchData() {

        reFetch()

        viewModel.getTournaments.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {

                    binding.swipeRefreshLayout.isRefreshing = false

                    state.data.items?.let { items ->
                        tournamentList.clear()
                        tournamentList.addAll(items.filter { item ->
                            item.tournamentStatus == 1 || item.tournamentStatus == 2
                        })
                        tournamentsAdapter.updateAllData(tournamentList)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
        viewModel.getGameList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.data.items?.let { items ->
                        gameList = arrayListOf(getString(R.string.games))
                        for (item in items) item.name?.let { gameList.add(it) }
                        gamesCategoryAdapter = ArrayAdapter(requireContext(), R.layout.item_category, gameList)
                        binding.spinnerTournamentCategory.adapter = gamesCategoryAdapter
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
        viewModel.joinTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (state.data.success == true) {
                        viewModel.getTournaments.fetchData()
                        showToast(getString(R.string.successful))
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
        viewModel.cancelTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (state.data.success == true) {
                        viewModel.getTournaments.fetchData()
                        showToast(getString(R.string.successful))
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler?.postDelayed(refreshTask, delay)
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(refreshTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(refreshTask)
        handler = null
    }
    private fun events() {

        handler = Handler(getMainLooper());

        binding.apply {


            recyclerViewTournamentList.adapter = tournamentsAdapter
            recyclerViewTournamentFilter.adapter = buttonFilterAdapter

            buttonFilterAdapter.onItemClickListener = { pos, item ->
                viewModel.isUserAppliedForTournaments = item.selected
                for (filter in buttonFilterList) filter.selected = false
                item.selected = true
                buttonFilterList[pos] = item
                viewModel.isUserAppliedForTournaments = buttonFilterList[1].selected
                viewModel.isApprovedTournaments = buttonFilterList[2].selected
                viewModel.getTournaments.fetchData()
                buttonFilterAdapter.updateAllData(buttonFilterList)
            }

            tournamentsAdapter.onItemClickListener = { _, item ->
                navigate(
                    R.id.action_tournamentsFragment_to_tournamentDetailFragment,
                    bundleOf("id" to item.id)
                )
            }

            imageViewTournamentSearch.setOnClickListener {
                textViewTournamentsTitle.visibility = View.INVISIBLE
                editTextTournamentSearch.visibility = View.VISIBLE
            }

            imageViewTournamentCalendar.setOnClickListener {
                navigate(R.id.nav_events)
            }

            spinnerTournamentCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (spinnerTournamentCategory.selectedItemPosition == 0) viewModel.gameNameFilter = null
                    else viewModel.gameNameFilter = spinnerTournamentCategory.selectedItem.toString()
                    viewModel.getTournaments.fetchData()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            editTextTournamentSearch.addTextChangedListener { input ->
                val text = input.toString()
                if (text.length > 1) {
                    tournamentsAdapter.updateAllData(
                        tournamentList.filter { tournamentItem ->
                            tournamentItem.name.myContains(text)
                                    || tournamentItem.gameName.myContains(text)
                        }
                    )
                } else {
                    tournamentsAdapter.updateAllData(tournamentList)
                    if (text.isEmpty()) {
                        textViewTournamentsTitle.visibility = View.VISIBLE
                        editTextTournamentSearch.visibility = View.INVISIBLE
                    }
                }
            }

            tournamentsAdapter.onJoinTournamentClickListener = { item ->
                when(item.userJoinStatus) {
                    0 -> { // Katil
                        viewModel.tournamentId = item.id
                        viewModel.joinTournament.fetchData()
                    }
                    1 -> { // Katildin
                        showToast(getString(R.string.joined_tournament_warning))
                    }
                    2 -> { // Beklemede (Onay Bekliyor)
                        showQuestionDialog(getString(R.string.waiting_tournament_warning)) {
                            viewModel.tournamentId = item.id
                            viewModel.cancelTournament.fetchData()
                        }
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {

                reFetch()

            }

        }
    }



}