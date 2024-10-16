package com.nuevo.gameness.ui.pages.personal.profile.myprofile.achievements

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.model.profile.AwardsItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentAchievementsBinding
import com.nuevo.gameness.databinding.ItemAllAwardsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllAwardsAdapter
import com.nuevo.gameness.utils.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementsFragment : BaseFragment<FragmentAchievementsBinding>() {

    private val viewModel by viewModels<AchievementsViewModel>()
    private val allAwardsAdapter= AllAwardsAdapter()
    private val userAwardsList = arrayListOf<AwardsItem>()

    override fun getViewBinding() = FragmentAchievementsBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }

    private fun events() {
        allAwardsAdapter.onItemClickListener = { _, item ->
            val dialogBinding = ItemAllAwardsBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogBinding.root)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.apply {
                imgAward.load(requireContext() ,item.iconImageURL)
                (this.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 10
                    marginEnd = 10
                }
                txtAwardTitle.text = item.gameName
                txtAwardDesc.text = item.awardName
                imageViewClose.visibility = View.VISIBLE
                imageViewClose.setOnClickListener { dialog.dismiss() }
            }
        }
    }

    private fun fetchData(){
        viewModel.getUserAwardList.fetchData()

        viewModel.getUserAwardList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items.let { items ->
                            if(!items.isNullOrEmpty()){
                                userAwardsList.clear()
                                userAwardsList.addAll(items)
                                allAwardsAdapter.updateAllData(userAwardsList)
                                recyclerAllUserAwards.adapter=allAwardsAdapter

                            }else{
                                txtUserAwardsWarning.visibility= View.VISIBLE
                                recyclerAllUserAwards.visibility= View.GONE
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