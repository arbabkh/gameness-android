package com.nuevo.gameness.ui.pages.personal.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.databinding.ItemPagerLastTournamentsBinding
import com.nuevo.gameness.databinding.ItemRecyclerLastTournamentsBinding
import com.nuevo.gameness.utils.*
import kotlin.collections.ArrayList

class AllTournamentsAdapter(
    private val itemList: ArrayList<TournamentItem> = arrayListOf(),
    private val isViewTypePagerView: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: ((pos: Int, item: TournamentItem) -> Unit)? = null
    lateinit var context: Context

    companion object {
        private const val VIEW_TYPE_PAGER = 1
        private const val VIEW_TYPE_RECYCLER = 2
    }

    inner class PagerViewHolder(private val binding: ItemPagerLastTournamentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TournamentItem) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener?.invoke(adapterPosition, item)
                }

                imageViewBlurringTournament.loadWithBlurringImage(context, item.gameImageUrl)
                imageViewHomeTournament.load(context,item.imageUrl)
                imageViewHomeTournamentIcon.load(context,item.gameImageUrl)
                textViewHomeTournamentName.text = item.name
                item.startDate.toCalendar()?.let { date ->
                    textViewHomeTournamentDate.text = date.time.myToString("dd.MM.yyyy")
                }
                if (item.tournamentStatus == 1) {
                    buttonHomeTournamentsJoin.text = context.getString(R.string.active)
                    buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.blue)
                } else {
                    buttonHomeTournamentsJoin.text = context.getString(R.string.completed)
                    buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.tertiary_dark)
                }
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemRecyclerLastTournamentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TournamentItem) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener?.invoke(adapterPosition, item)
                }

                imageViewBlurringTournament.loadWithBlurringImage(context, item.gameImageUrl)
                imageViewHomeTournament.load(context,item.gameImageUrl)
                imageViewHomeTournamentIcon.load(context,item.imageUrl)
                textViewHomeTournamentName.text = item.name
                item.startDate.toCalendar()?.let { date ->
                    textViewHomeTournamentDate.text = date.time.myToString("dd.MM.yyyy")
                }
                if (item.tournamentStatus == 1) {
                    buttonHomeTournamentsJoin.text = context.getString(R.string.active)
                    buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.blue)
                } else {
                    buttonHomeTournamentsJoin.text = context.getString(R.string.completed)
                    buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.tertiary_dark)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isViewTypePagerView) VIEW_TYPE_PAGER
        else VIEW_TYPE_RECYCLER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_PAGER) PagerViewHolder(
            ItemPagerLastTournamentsBinding.inflate(inflater, parent, false)
        )
        else RecyclerViewHolder(
            ItemRecyclerLastTournamentsBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PagerViewHolder -> holder.bind(itemList[position])
            is RecyclerViewHolder -> holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateAllData(newList: List<TournamentItem>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

}