package com.nuevo.gameness.ui.pages.personal.profile.myteam.gamers

import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentGamersBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.TeamUsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamerFragment:BaseFragment<FragmentGamersBinding>() {
    override fun getViewBinding(): FragmentGamersBinding =
        FragmentGamersBinding.inflate(layoutInflater)

    private val viewModel by viewModels<GamersViewModel>()

    private val adapter=TeamUsersAdapter()
    override fun initView() {


        events()
        fetchData()

    }

    private fun events(){
        binding.apply {

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
                                recyclerGamerList.visibility=View.VISIBLE
                                adapter.updateAllData(items)
                                recyclerGamerList.adapter=adapter
                            }else{
                                txtGamersWarning.visibility= View.VISIBLE
                                recyclerGamerList.visibility=View.GONE
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