package com.nuevo.gameness.ui.pages.personal.events

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.events.EventItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentEventsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.myContains
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : BaseFragment<FragmentEventsBinding>() {

    private val viewModel by viewModels<EventsViewModel>()
    private val eventsAdapter = EventsAdapter()
    private val allEvents = arrayListOf<EventItem>()

    override fun getViewBinding() = FragmentEventsBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(visible = false)

        events()
        fetchData()
    }

    private fun fetchData() {
        viewModel.eventList.fetchData()

        viewModel.eventList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items?.let { items ->
                            if (items.isEmpty()) {
                                txtEventWarning.visibility = View.VISIBLE
                                recyclerEvent.visibility = View.GONE
                            } else {
                                txtEventWarning.visibility = View.GONE
                                recyclerEvent.visibility = View.VISIBLE
                                allEvents.clear()
                                allEvents.addAll(items)
                                eventsAdapter.updateAllData(items)
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

    private fun events() {
        binding.apply {
            recyclerEvent.adapter = eventsAdapter

            editTextEventsSearch.addTextChangedListener { input ->
                val text = input.toString()
                if (text.length > 1) {
                    eventsAdapter.updateAllData(
                        allEvents.filter { tournamentItem ->
                            tournamentItem.title.myContains(text)
                                    || tournamentItem.shortDescription.myContains(text)
                        }
                    )
                } else {
                    eventsAdapter.updateAllData(allEvents)
                    if (text.isEmpty()) {
                        textViewEventsTitle.visibility = View.VISIBLE
                        editTextEventsSearch.visibility = View.INVISIBLE
                    }
                }
            }

            imageViewEventsSearch.setOnClickListener {
                textViewEventsTitle.visibility = View.INVISIBLE
                editTextEventsSearch.visibility = View.VISIBLE
            }

            eventsAdapter.onItemClickListener = { _, item ->
                navigate(
                    R.id.action_eventsFragment_to_eventDetailFragment,
                    bundleOf("id" to item.id)
                )
            }
        }
    }

}