package com.nuevo.gameness.ui.pages.personal.profile.settings.gamer

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentGamerSettingsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.TeamUsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamerSettingsFragment : BaseFragment<FragmentGamerSettingsBinding>() {

    private val viewModel by viewModels<GamerSettingsViewModel>()

    private val adapter= TeamUsersAdapter()

    override fun getViewBinding() = FragmentGamerSettingsBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }
    private fun events(){
        binding.apply {
            adapter.onItemClickListener = { _, item ->
                navigate(R.id.inviteUserFragment, bundleOf("selectedGameId" to item.gameID))
            }
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData(){
        viewModel.teamUsers.fetchData()

        viewModel.teamUsers.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.data.let { items ->
                            if(items?.isNotEmpty() == true){
                                txtGamersWarning.visibility= View.GONE
                                recyclerGamerList.visibility= View.VISIBLE
                                adapter.updateAllData(items)
                                recyclerGamerList.adapter=adapter
                            }else{
                                txtGamersWarning.visibility= View.VISIBLE
                                recyclerGamerList.visibility= View.GONE
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