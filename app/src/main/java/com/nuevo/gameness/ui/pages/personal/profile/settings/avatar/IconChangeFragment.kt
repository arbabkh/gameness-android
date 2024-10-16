package com.nuevo.gameness.ui.pages.personal.profile.settings.avatar

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.IconItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentChangeIconBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IconChangeFragment : BaseFragment<FragmentChangeIconBinding>() {

    private val viewModel by viewModels<IconChangeViewModel>()
    private val iconChangeAdapter = IconChangeAdapter()
    private var selectedIcon = IconItem()
    private val profileIconList = arrayListOf<IconItem>()

    override fun getViewBinding() = FragmentChangeIconBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events() {
        binding.apply {
            iconRecyclerView.adapter = iconChangeAdapter

            iconChangeAdapter.onItemClickListener = { _, item ->
                selectedIcon = item
                buttonColorChange()
            }

            uploadButton.setOnClickListener {
                viewModel.fileId = selectedIcon.id
                if (!viewModel.fileId.isNullOrEmpty()) {
                    viewModel.setProfileIcon.fetchData()
                } else showDialog(getString(R.string.one_game_must_be_selected))
            }
            icBack.setOnClickListener { popBackStack() }

        }
    }

    private fun fetchData() {
        viewModel.getProfileIcons.fetchData()

        viewModel.getProfileIcons.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        profileIconList.clear()
                        profileIconList.addAll(items)
                        iconChangeAdapter.updateAllData(profileIconList)

                        binding.uploadButton.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tertiary_dark
                            )
                        )
                        binding.uploadButton.setMyTextColor(context, R.color.gray)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.setProfileIcon.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        if (state.data.success == true) {
                            popBackStack()
                        } else showToast(state.data.message.toString())
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun buttonColorChange() {
        binding.apply {
            if (profileIconList.any { it.selected }) {
                uploadButton.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
                uploadButton.setMyTextColor(context, R.color.white)
            } else {
                uploadButton.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.tertiary_dark
                    )
                )
                uploadButton.setMyTextColor(context, R.color.gray)
            }
        }
    }

}