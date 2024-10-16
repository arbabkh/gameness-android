package com.nuevo.gameness.ui.pages.personal.profile.settings.avatar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setMargins
import com.nuevo.gameness.data.model.settings.IconItem
import com.nuevo.gameness.databinding.ItemIconChangeBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load

class IconChangeAdapter(
    private val margin: Int = 10,
    private val isSelectable: Boolean? = null
) : BaseRecyclerAdapter<IconItem, ItemIconChangeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemIconChangeBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: IconItem, binding: ItemIconChangeBinding) {
        setSelectedIcon(item.selected, binding)
        binding.apply {
            root.setOnClickListener {
                val items = getItems()
                items?.forEach {
                    it.selected = false
                }
                item.selected = !item.selected
                setSelectedIcon(item.selected, binding)
                notifyDataSetChanged()

                if (item.selected) {
                    onItemClickListener?.invoke(position, item)
                }
            }
            imageViewIcon.load(context, item.filePath)
        }
    }

    private fun setSelectedIcon(selected: Boolean, binding: ItemIconChangeBinding) {
        binding.apply {
            val params = cardViewIconChange.layoutParams as ViewGroup.MarginLayoutParams
            if (selected) {
                root.alpha = 0.5f
                params.setMargins(margin)
            } else {
                root.alpha = 1f
                params.setMargins(0)
            }
        }
    }
}