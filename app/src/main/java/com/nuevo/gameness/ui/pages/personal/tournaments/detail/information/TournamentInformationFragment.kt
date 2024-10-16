package com.nuevo.gameness.ui.pages.personal.tournaments.detail.information

import android.text.Html
import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentInformationBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentInformationFragment : BaseFragment<FragmentTournamentInformationBinding>() {

    private val viewModel by viewModels<TournamentInformationViewModel>()

    override fun getViewBinding() = FragmentTournamentInformationBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.tournamentId = requireArguments().getString("id")
        fetchData()
    }

    private fun fetchData() {
        viewModel.getTournament.fetchData()

        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        if (state.data.success == true) {
                            state.data.data?.let { item ->
                                textViewGeneralInfo.text =Html.fromHtml( item.generalInformations)
                                textViewGeneralInfo.linksClickable = true

                                textViewConditionsOfParticipation.text = Html.fromHtml(item.conditionsOfParticipation)
                                textViewConditionsOfParticipation.linksClickable = true
                                if (!item.awards.isNullOrEmpty()) {
                                    textViewAwards.text = item.awards[0].name
                                    for (i in 1 until item.awards.size)
                                        textViewAwards.append("\n${item.awards[i].name ?: ""}")
                                }
                                if (!item.referees.isNullOrEmpty()) {
                                    textViewReferee.text = "${item.referees[0].name ?: ""} ${item.referees[0].surname ?: ""}"
                                    for (i in 1 until item.referees.size)
                                        textViewReferee.append("\n${item.referees[i].name ?: ""} ${item.referees[i].surname ?: ""}")
                                }
                            }
                        } else showToast(state.data.message.toString())
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

}