package com.nuevo.gameness.ui.pages.register.choosegame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setMargins
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.databinding.ItemChooseGameBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class ChooseGameAdapter(
    private val margin: Int = 10,
    private val isSelectable: Boolean? = null
) : BaseRecyclerAdapter<GameListItem, ItemChooseGameBinding>() {

    override fun getViewBinding(
        inflater : LayoutInflater,
        parent: ViewGroup
    ) = ItemChooseGameBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: GameListItem, binding: ItemChooseGameBinding) {
        binding.apply {
            root.setOnClickListener {
                item.selected = isSelectable ?: !item.selected
                setChooseGame(item.selected, binding)
                onItemClickListener?.invoke(position, item)
            }
            imageViewGame.load(context, item.imageUrl)
            setChooseGame(isSelectable ?: item.selected, binding)
        }
    }

    private fun setChooseGame(selected: Boolean, binding: ItemChooseGameBinding) {
        binding.apply {
            val params = cardViewChooseGame.layoutParams as ViewGroup.MarginLayoutParams
            if (selected) {
                root.alpha = 1f
                params.setMargins(margin)
            } else {
                root.alpha = 0.3f
                params.setMargins(0)
            }
        }
    }

}