package com.nuevo.gameness.ui.pages.personal.profile.myprofile.discover

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.model.profile.AwardsItem
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentDiscoverBinding
import com.nuevo.gameness.databinding.ItemUserAwardsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllTournamentsAdapter
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AwardsAdapter
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameAdapter
import com.nuevo.gameness.utils.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {

    private val viewModel by viewModels<DiscoverViewModel>()

    private val myGamesAdapter = ChooseGameAdapter(0, true)
    private val myGameList = arrayListOf<GameListItem>()

    private val tournamentsAdapter = AllTournamentsAdapter(isViewTypePagerView = true)

    private val userAwardsAdapter= AwardsAdapter()
    private val userAwardsList = arrayListOf<AwardsItem>()

    override fun getViewBinding() = FragmentDiscoverBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }

    private fun events() {
        binding.apply {
            recyclerSelectedGames.adapter = myGamesAdapter
            recyclerUserAwards.adapter = userAwardsAdapter
            viewPagerLastTournaments.adapter = tournamentsAdapter

            btnSeeAllAwards.setOnClickListener {
                //turnuva başarımları sayfasına gidilcek
                UserModel.instance.profilePageItem = 1
                navigate(R.id.profileFragment)
            }
            btnSeeAllTournament.setOnClickListener {
                 //katıldığım turnuvalar sayfasına gidilcek
                UserModel.instance.profilePageItem = 2
                navigate(R.id.profileFragment)
            }
            btnSeeAllGames.setOnClickListener {
                navigate(R.id.action_profileFragment_to_selectedGamesFragment)
            }

            userAwardsAdapter.onItemClickListener = { _, item ->
                val dialogBinding = ItemUserAwardsBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogBinding.root)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.apply {
                    imageViewAward.load(requireContext(), item.iconImageURL)
                    (root.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        marginStart = 200
                        marginEnd = 200
                    }
                    textViewAwardTitle.text = item.awardName
                    textViewAwardDescription.text = item.gameName
                    imageViewClose.visibility = View.VISIBLE
                    imageViewClose.setOnClickListener { dialog.dismiss() }
                }
            }

            tournamentsAdapter.onItemClickListener = { _, item ->
                navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentDetailFragment,
                    "id" to item.id
                ))
            }
        }
    }

    private fun fetchData(){
        viewModel.getUserAwardList.fetchData()
        viewModel.userTournamentsList.fetchData()
        viewModel.getMyGameList.fetchData()

        //son baarımlar
        viewModel.getUserAwardList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items.let { items ->

                            if(!items.isNullOrEmpty()){
                                txtUserAwardsWarning.visibility=View.GONE
                                recyclerUserAwards.visibility=View.VISIBLE
                                userAwardsList.clear()
                                userAwardsList.addAll(items)
                                userAwardsAdapter.updateAllData(userAwardsList)

                            }else{
                                txtUserAwardsWarning.visibility=View.VISIBLE
                                recyclerUserAwards.visibility=View.GONE
                            }
                        }
                    }

                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }

        //son turnuvalar
        viewModel.userTournamentsList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items?.let { items ->
                            val filteredList = items.filter { item ->
                                item.tournamentStatus == 1 || item.tournamentStatus == 2
                            }
                            if(filteredList.isNotEmpty()){
                                txtLastTournamentsWarning.visibility=View.GONE
                                viewPagerLastTournaments.visibility=View.VISIBLE
                                tournamentsAdapter.updateAllData(filteredList)
                            } else {
                                txtLastTournamentsWarning.visibility=View.VISIBLE
                                viewPagerLastTournaments.visibility=View.GONE
                            }
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        //kendi seçtiğim oyunlar
        viewModel.getMyGameList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items?.let { items ->
                            if(items.isNotEmpty()){
                                txtSelectedGamesWarning.visibility=View.GONE
                                recyclerSelectedGames.visibility=View.VISIBLE
                                myGameList.clear()
                                myGameList.addAll(items)
                                myGamesAdapter.updateAllData(myGameList)
                            }else{
                                recyclerSelectedGames.visibility=View.GONE
                                txtSelectedGamesWarning.visibility=View.VISIBLE
                            }
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