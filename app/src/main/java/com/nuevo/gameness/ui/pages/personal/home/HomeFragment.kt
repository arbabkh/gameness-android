package com.nuevo.gameness.ui.pages.personal.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.announcements.AnnouncementItem
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentHomeBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.home.adapter.HomeAnnouncementsAdapter
import com.nuevo.gameness.ui.pages.personal.home.adapter.HomeTournamentsAdapter
import com.nuevo.gameness.ui.pages.personal.home.adapter.HomeTrainingsAdapter
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.loadWithBlurringImage
import com.nuevo.gameness.utils.openUrlInBrowser
import com.nuevo.gameness.utils.showQuestionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()
    private val homeAnnouncementsAdapter = HomeAnnouncementsAdapter()
    private val homeTournamentsAdapter = HomeTournamentsAdapter()
    private val homeTrainingsAdapter = HomeTrainingsAdapter()

    private var handler: Handler? = null

    val delay: Long = Constants.AUTO_REFRESH_RATE.toLong() // Refresh every 30 seconds

    private val refreshTask = object: Runnable {
        override fun run() {
            reFetch()
            handler?.postDelayed(this, delay)
        }

    }

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(visible = false)

        events()
        fetchData()
        homeTrainingsAdapter.updateAllData(arrayListOf(true))
    }

    private fun reFetch() {
        viewModel.getAnnouncements.fetchData()
        viewModel.getTournaments.fetchData()

    }

    private fun fetchData() {

        reFetch()

        viewModel.getAnnouncements.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.data.items?.let { items ->
                        homeAnnouncementsAdapter.updateAllData(items as ArrayList<AnnouncementItem>)
                    }
                }

                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                is NetworkResult.Loading -> {
                   // binding.swipeRefreshLayout.isRefreshing = true
                    showLoading.value = state.isLoading
                }

                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
        viewModel.getTournaments.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.data.items?.let { items ->
                        homeTournamentsAdapter.updateAllData(items as ArrayList<TournamentItem>)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResult.Loading -> {
                 //   binding.swipeRefreshLayout.isRefreshing = true
                    showLoading.value = state.isLoading
                }
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
                is NetworkResult.Loading -> {
                   // binding.swipeRefreshLayout.isRefreshing = true
                    showLoading.value = state.isLoading
                }
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

                is NetworkResult.Loading -> {
                   // binding.swipeRefreshLayout.isRefreshing = true
                    showLoading.value = state.isLoading
                }

                is NetworkResult.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    state.show()
                }
            }
        }
    }

    private fun events() {

        handler = Handler(Looper.getMainLooper())

        binding.apply {
            imageViewPremiumBuy.loadWithBlurringImage(requireContext(), R.drawable.premium_buy)

            viewPager2HomeAnnouncements.adapter = homeAnnouncementsAdapter
            viewPager2HomeTournaments.adapter = homeTournamentsAdapter
            viewPager2HomeTrainings.adapter = homeTrainingsAdapter

            TabLayoutMediator(tabLayoutHomeAnnouncements, viewPager2HomeAnnouncements)
            { _, _ ->}.attach()

            imageViewHomeCalendar.setOnClickListener {
                navigate(R.id.nav_events)
            }
            textViewSeeAllTournaments.setOnClickListener {
                navigate(R.id.nav_tournaments)
            }
            textViewSeeAllTraining.setOnClickListener {
                navigate(R.id.premiumFragment)
            }
            layoutPremiumBuy.setOnClickListener {
                navigate(R.id.premiumFragment)
            }

            buttonMarketplace.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.marketplace))
                navigate(R.id.premiumFragment, bundle)
            }

            buttonLockness.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.lockness))
                navigate(R.id.premiumFragment, bundle)
            }

            buttonPrediction.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.prediction))
                navigate(R.id.premiumFragment, bundle)            }

            homeTrainingsAdapter.onItemClickListener = { _, _ ->
                navigate(R.id.premiumFragment)
            }

            homeTournamentsAdapter.onJoinTournamentClickListener = { item ->
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

            homeAnnouncementsAdapter.onItemClickListener = { _, item ->
                try {
                    openUrlInBrowser(item.link.toString())
                } catch (ex: Exception) {
                    showToast(ex.message.toString())
                }
            }

            homeTournamentsAdapter.onItemClickListener = { _, item ->
                navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentDetailFragment,
                    "id" to item.id
                ))
            }

            swipeRefreshLayout.setOnRefreshListener {
                reFetch()
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


}