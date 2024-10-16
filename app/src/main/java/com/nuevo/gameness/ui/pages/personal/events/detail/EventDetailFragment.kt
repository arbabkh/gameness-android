package com.nuevo.gameness.ui.pages.personal.events.detail

import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentEventDetailBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.toDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : BaseFragment<FragmentEventDetailBinding>() {

    private val viewModel by viewModels<EventDetailViewModel>()

    override fun getViewBinding() = FragmentEventDetailBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(true) { popBackStack() }

        viewModel.eventId = arguments?.getString("id")

        fetchData()
    }

    private fun fetchData() {
        viewModel.getEvent.fetchData()

        viewModel.getEvent.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data?.let { eventItem ->
                            imageViewEventDetail.load(requireContext(), eventItem.imageURL)
                            val eventDate = eventItem.eventDate?.let { toDate(it.split("T")[0]) }
                            textViewEventDate.text = eventDate
                            textViewEventsTitle.text = eventItem.title
                            textViewShortDescription.text = eventItem.shortDescription
                            textViewEventInfo.text = eventItem.eventInformations
                            textViewConditionsOfParticipation.text = eventItem.conditionsOfParticipation
                            textViewPlace.text = eventItem.place
                            textViewHourAndDate.text = eventDate
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