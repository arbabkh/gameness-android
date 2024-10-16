package com.nuevo.gameness.ui.pages.register.choosegame

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentChooseGameBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.myContains
import com.nuevo.gameness.utils.setMyBackgroundTintList
import com.nuevo.gameness.utils.setMyTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGameFragment : BaseFragment<FragmentChooseGameBinding>() {

    private val viewModel by viewModels<ChooseGameViewModel>()
    private val chooseGameAdapter = ChooseGameAdapter()
    private val chooseGameList = arrayListOf<GameListItem>()
    private val filteredChooseGameList = arrayListOf<GameListItem>()

    var onNextPageListener: ((request: List<GameListItem>) -> Unit)? = null

    override fun getViewBinding() = FragmentChooseGameBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }

    private fun fetchData() {
        viewModel.getGameList.fetchData()
        viewModel.getGameList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        chooseGameList.clear()
                        filteredChooseGameList.clear()
                        chooseGameList.addAll(items)
                        filteredChooseGameList.addAll(items)
                        chooseGameAdapter.updateAllData(filteredChooseGameList)
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
            recyclerViewChooseGame.adapter = chooseGameAdapter

            buttonChooseGameNext.setMyBackgroundTintList(context, R.color.tertiary_dark)
            buttonChooseGameNext.setMyTextColor(context, R.color.gray)

            chooseGameAdapter.onItemClickListener = { pos, item ->
                filteredChooseGameList[pos].selected = item.selected
                chooseGameList[chooseGameList.indexOf(item)].selected = item.selected
                chooseGameAdapter.updateAllData(filteredChooseGameList)
                if (chooseGameList.none { it.selected }) {
                    buttonChooseGameNext.setMyBackgroundTintList(context, R.color.tertiary_dark)
                    buttonChooseGameNext.setMyTextColor(context, R.color.gray)
                } else {
                    buttonChooseGameNext.setMyBackgroundTintList(context, R.color.blue)
                    buttonChooseGameNext.setMyTextColor(context, R.color.white)
                }
            }

            buttonChooseGameNext.setOnClickListener {
                val selectedGameIdList = chooseGameList.filter { it.selected }
                if (selectedGameIdList.isNotEmpty()) onNextPageListener?.invoke(selectedGameIdList)
            }

            editTextChooseGameSearch.addTextChangedListener { input ->
                filteredChooseGameList.clear()
                if (input.toString().length > 2) filteredChooseGameList.addAll(
                    chooseGameList.filter { item ->
                        item.name.myContains(input.toString())
                    }
                )
                else filteredChooseGameList.addAll(chooseGameList)
                chooseGameAdapter.updateAllData(filteredChooseGameList)
            }
        }
    }

}