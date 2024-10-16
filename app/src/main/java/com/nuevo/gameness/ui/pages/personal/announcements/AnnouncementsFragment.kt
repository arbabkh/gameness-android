package com.nuevo.gameness.ui.pages.personal.announcements

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.announcements.AnnouncementItem
import com.nuevo.gameness.data.model.announcements.CategorizedAnnouncements
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentAnnouncementsBinding
import com.nuevo.gameness.databinding.ItemAnnouncementsChildBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.announcements.adapter.AnnouncementsSectionAdapter
import com.nuevo.gameness.utils.isYesterday
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.toCalendar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>() {

    private val viewModel by viewModels<AnnouncementsViewModel>()
    private val announcementsSectionAdapter = AnnouncementsSectionAdapter()

    override fun getViewBinding() = FragmentAnnouncementsBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(visible = false)

        events()
        fetchData()
    }

    private fun fetchData() {
        viewModel.getAnnouncements.fetchData()

        viewModel.getAnnouncements.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        announcementsSectionAdapter.updateAllData(
                            getCategorizedAnnouncementList(
                                items
                            )
                        )
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
            recyclerViewAnnouncements.adapter = announcementsSectionAdapter
            imageViewAnnouncementsCalendar.setOnClickListener {
                navigate(R.id.nav_events)
            }
            announcementsSectionAdapter.onChildItemClickListener = { _, item ->
                val dialogBinding = ItemAnnouncementsChildBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogBinding.root)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.apply {
                    imageViewAnnouncementIcon.load(requireContext(), item.iconUrl)
                    textViewAnnouncementsTitle.text = item.title
                    textViewAnnouncementsDescription.text = item.description
                    imageViewClose.visibility = View.VISIBLE
                    imageViewClose.setOnClickListener { dialog.dismiss() }
                }
                dialog.show()
            }
        }
    }

    private fun getCategorizedAnnouncementList(list: List<AnnouncementItem>): ArrayList<CategorizedAnnouncements> {
        for (item in list) {
            val announcementTimeInMillis:Long = item.startDate.toCalendar()?.timeInMillis ?: continue
            item.dateTime = when {
                DateUtils.isToday(announcementTimeInMillis) ->getString(R.string.today)
                isYesterday(announcementTimeInMillis) ->getString(R.string.yesterday)
                else -> getString(R.string.other)
            }
        }
        val categorizedAnnouncementList = arrayListOf<CategorizedAnnouncements>()
        for (entry in list.groupBy { it.dateTime }) {
            entry.key?.let { title ->
                CategorizedAnnouncements(title, entry.value)
            }?.let { item ->
                categorizedAnnouncementList.add(item)
            }
        }
        return categorizedAnnouncementList
    }

}