package com.nuevo.gameness.ui.pages.personal.profile.settings.gamer

import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.Datum
import com.nuevo.gameness.data.model.settings.TeamUserItem
import com.nuevo.gameness.data.model.settings.request.ChangeTeamGameUserRequestElement
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentInviteUserBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.settings.gamer.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteUserFragment : BaseFragment<FragmentInviteUserBinding>() {

    private val viewModel by viewModels<InviteUserViewModel>()
    private val userAdapter= UsersAdapter()

    private var selectedGameId=""
    private var gameList= arrayListOf<Datum>()
    private var teamUserItem= mutableListOf<TeamUserItem>()

    override fun getViewBinding() = FragmentInviteUserBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        selectedGameId=requireArguments().getString("selectedGameId","")
        if(selectedGameId.isEmpty()) showToast(getString(R.string.error_warning))
        else {
            events()
            fetchData()
        }
    }

    private fun events(){
        binding.apply {
            recyclerUserList.adapter=userAdapter

            userAdapter.onItemClickListener = { _, item ->
                if(item.selected){
                   teamUserItem.add(item)
                    showToast(teamUserItem.size.toString())
                }
                else{
                    for(i in teamUserItem){
                        if(i.teamUserID==item.teamUserID){
                            teamUserItem.remove(i)
                        }
                    }

                    showToast(teamUserItem.size.toString())

                }
            }
            btnSave.setOnClickListener {
                for(item in gameList){
                    if(item.gameID==selectedGameId){
                       // if(item.teamUserItemList?.isNotEmpty() == true){
                       //     teamUserItem.addAll(item.teamUserItemList!!)
                       // }
                        gameList.remove(item)
                        val datum=Datum(item.gameName!!,item.gameID,teamUserItem)
                        gameList.add(datum)
                        break
                    }
                }

                val lastGameList= arrayListOf<ChangeTeamGameUserRequestElement>()
                for(item in gameList){
                    val userIdList= arrayListOf<String>()
                    if(item.teamUserItemList?.isNotEmpty() == true){
                        for(i in item.teamUserItemList!!){
                            userIdList.add(i.teamUserID)
                        }
                    }
                    val x=ChangeTeamGameUserRequestElement(item.gameID!!,userIdList)
                    lastGameList.add(x)
                }

                viewModel.request=lastGameList
                viewModel.setUserTeam.fetchData()

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
                             gameList.addAll(items)
                            }
                            val selectedGame=gameList.filter {
                                it.gameID==selectedGameId
                            }[0]

                            if(selectedGame.teamUserItemList?.isNotEmpty() == true){
                                teamUserItem.addAll(selectedGame.teamUserItemList!!)
                            }

                            viewModel.teamUserList.fetchData()
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.teamUserList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.teamUserItemList.let { items ->
                            if(items.isNotEmpty()){
                                recyclerUserList.visibility= View.VISIBLE
                                val selected=gameList.filter {
                                    it.gameID==selectedGameId
                                }[0]

                               for(i in items){
                                   val selectedUser=selected.teamUserItemList?.filter {
                                       it.teamUserID==i.teamUserID
                                   }
                                   i.selected = selectedUser?.isNotEmpty() == true
                               }

                                userAdapter.updateAllData(items)

                            } else recyclerUserList.visibility = View.GONE
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.setUserTeam.state.observe(viewLifecycleOwner){ state->
            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    if (response.success == true) showToast(getString(R.string.successful))
                    else showToast(response.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }
    }
}