package com.nuevo.gameness.ui.pages.personal.profile.settings.gamer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.TeamUserItem
import com.nuevo.gameness.databinding.ItemTeamUserBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class UsersAdapter: BaseRecyclerAdapter<TeamUserItem, ItemTeamUserBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemTeamUserBinding = ItemTeamUserBinding.inflate(inflater,parent,false)

    override fun bind(position: Int, item: TeamUserItem, binding: ItemTeamUserBinding) {
        binding.apply {
            selectorView.setOnClickListener {
                item.selected=!item.selected
                setChooseGame(item.selected, binding)
                onItemClickListener?.invoke(position, item)
            }

            setChooseGame(item.selected, binding)
            txtUsername.text=item.user.userName

            imgGamer.load(context,item.user.imageUrl)
        }
    }

    private fun setChooseGame(selected: Boolean, binding: ItemTeamUserBinding) {
        binding.apply {
            if(selected){
                root.setBackgroundResource(R.drawable.user_background_selected)
                imgSelect.visibility=View.VISIBLE
                btnSelect.visibility=View.GONE
            }else{
                root.setBackgroundResource(R.drawable.user_background_unselected)
                imgSelect.visibility=View.GONE
                btnSelect.visibility=View.VISIBLE
            }
        }
    }

}