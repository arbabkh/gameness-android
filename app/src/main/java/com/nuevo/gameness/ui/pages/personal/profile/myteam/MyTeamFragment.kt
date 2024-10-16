package com.nuevo.gameness.ui.pages.personal.profile.myteam

import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.TeamData
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentMyTeamBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.TablayoutAdapter
import com.nuevo.gameness.ui.pages.personal.profile.myteam.attendteamtournaments.AttendTeamTournamentsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myteam.gamers.GamerFragment
import com.nuevo.gameness.ui.pages.personal.profile.myteam.teamachievements.TeamAchievementsFragment
import com.nuevo.gameness.ui.pages.personal.profile.myteam.teaminfo.TeamInfoFragment
import com.nuevo.gameness.utils.showQuestionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTeamFragment : BaseFragment<FragmentMyTeamBinding>() {

    private val viewModel by viewModels<MyTeamViewModel>()
    private var teamInfo: TeamData? = null

    private lateinit var adapter: TablayoutAdapter

    override fun getViewBinding() = FragmentMyTeamBinding.inflate(layoutInflater)

    override fun initView() {
        adapter = TablayoutAdapter(childFragmentManager)
        adapter.addFragment(TeamInfoFragment(), getString(R.string.team_info))
        adapter.addFragment(GamerFragment(), getString(R.string.players))
        adapter.addFragment(TeamAchievementsFragment(), getString(R.string.achievements))
        adapter.addFragment(AttendTeamTournamentsFragment(), getString(R.string.joined_tournaments))

        //Adapter'ımızdaki verileri viewPager adapter'a veriyoruz
        binding.viewPager.adapter = adapter
        //Tablar arasında yani viewPager'lar arasında geçisi sağlıyoruz
        binding.tabs.setupWithViewPager(binding.viewPager)

        events()
        fetchData()
    }

    private fun events() {
        binding.apply {
            buttonCreateTeam.setOnClickListener {
                navigate(R.id.action_profileFragment_to_createTeamFragment)
            }
            leaveTeamButton.setOnClickListener {
                showQuestionDialog(getString(R.string.leave_team_question)) {
                    leaveTheTeam()
                }
            }
        }
    }

    private fun leaveTheTeam() {
        viewModel.leaveTheTeam.fetchData()
        viewModel.leaveTheTeam.state.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    viewModel.myTeam.fetchData()
                }
                is NetworkResult.Loading -> {
                    showLoading.value = it.isLoading
                }
                is NetworkResult.Error -> {
                    it.show()
                }
                is NetworkResult.SuccessWithNoContent -> {
                    viewModel.myTeam.fetchData()
                }
            }
        }
    }

    private fun fetchData() {
        viewModel.myTeam.fetchData()

        viewModel.myTeam.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val success = state.data.success
                        if (success) {
                            warningLayout.visibility = View.GONE
                            teamLayout.visibility = View.VISIBLE
                            teamInfo = state.data.data
                            txtTeamName.text = teamInfo!!.name
                            val url = teamInfo!!.logoURL
                            Glide.with(requireContext()).load(url).into(imgTeamAvatar)
                        } else {
                            warningLayout.visibility = View.VISIBLE
                            teamLayout.visibility = View.GONE

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