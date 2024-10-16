package com.nuevo.gameness.ui.pages.register.gameinformation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.databinding.ItemGameInformationBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import java.util.*

class GameInformationAdapter: BaseRecyclerAdapter<GameListItem, ItemGameInformationBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemGameInformationBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: GameListItem, binding: ItemGameInformationBinding) {
        binding.textViewGameUniqueId.text = item.name?.uppercase(Locale.getDefault())
        binding.editTextGameUniqueId.addTextChangedListener { input ->
            val text = (input ?: "").toString()
            item.uniqueID = text
        }
        binding.editTextGameUniqueId.setText(item.uniqueID ?: "")
    }

}