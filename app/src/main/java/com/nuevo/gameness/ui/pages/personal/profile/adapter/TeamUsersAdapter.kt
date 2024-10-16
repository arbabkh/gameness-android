package com.nuevo.gameness.ui.pages.personal.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.data.model.settings.Datum
import com.nuevo.gameness.databinding.ItemGamersParentBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import java.util.*

class TeamUsersAdapter: BaseRecyclerAdapter<Datum, ItemGamersParentBinding>() {

    private var visibility=false

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemGamersParentBinding = ItemGamersParentBinding.inflate(inflater,parent,false)

    override fun bind(position: Int, item: Datum, binding: ItemGamersParentBinding) {
        binding.apply {
            btnInvite.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }

            txtGameName.text=(item.gameName?:"").uppercase(Locale.getDefault())

            val childAdapter=TeamUsersChildAdapter()
            childAdapter.updateAllData(item.teamUserItemList!!)
            recyclerChild.adapter=childAdapter

            txtGameName.setOnClickListener {
                if(visibility){
                    childView.visibility= View.GONE
                    visibility=false
                }else{
                    childView.visibility=View.VISIBLE
                    visibility=true
                }
            }

        }
    }

}