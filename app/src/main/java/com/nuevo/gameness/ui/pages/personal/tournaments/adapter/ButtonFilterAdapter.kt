package com.nuevo.gameness.ui.pages.personal.tournaments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentsStatusFilter
import com.nuevo.gameness.databinding.ItemButtonFilterBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.setMyTextColor

class ButtonFilterAdapter : BaseRecyclerAdapter<TournamentsStatusFilter, ItemButtonFilterBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemButtonFilterBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TournamentsStatusFilter, binding: ItemButtonFilterBinding) {
        binding.apply {
            textView.text = item.key
            when (item.selected) {
                true -> {
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    textView.setMyTextColor(context, R.color.tertiary_dark)
                }
                false -> {
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.tertiary_dark))
                    textView.setMyTextColor(context, R.color.white)
                }
                null -> {}
            }
            root.setOnClickListener { onItemClickListener?.invoke(position, item) }
        }
    }

}