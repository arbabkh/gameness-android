package com.nuevo.gameness.ui.pages.personal.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.settings.TeamUserItem
import com.nuevo.gameness.databinding.ItemGamersChildBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class TeamUsersChildAdapter : BaseRecyclerAdapter<TeamUserItem, ItemGamersChildBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemGamersChildBinding = ItemGamersChildBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TeamUserItem, binding: ItemGamersChildBinding) {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }

            txtUsername.text = item.user.userName
            if(!item.user.imageUrl.isNullOrEmpty()) imgGamer.load(context, item.user.imageUrl)
        }
    }

}