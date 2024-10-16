package com.nuevo.gameness.ui.pages.personal.profile.settings.invited.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nuevo.gameness.data.model.users.UserListByUserNameItem
import com.nuevo.gameness.databinding.ItemUserListByUsernameBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class UserListByUsernameAdapter : BaseRecyclerAdapter<UserListByUserNameItem, ItemUserListByUsernameBinding>() {

    var onInviteClickListener: ((pos: Int, item: UserListByUserNameItem) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemUserListByUsernameBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: UserListByUserNameItem, binding: ItemUserListByUsernameBinding) {
        binding.apply {
            imageViewUserProfile.load(context, item.imageURL)
            textViewUsername.text = item.username
            textViewInvite.setOnClickListener {
                onInviteClickListener?.invoke(position, item)
            }
        }
    }

}