package com.nuevo.gameness.ui.pages.personal.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.databinding.ItemHomeTournamentsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.*
import java.util.*

class HomeTournamentsAdapter : BaseRecyclerAdapter<TournamentItem, ItemHomeTournamentsBinding>() {

    var onJoinTournamentClickListener: ((item: TournamentItem) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemHomeTournamentsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TournamentItem, binding: ItemHomeTournamentsBinding) {
        binding.apply {
            imageViewBlurringTournament.loadWithBlurringImage(context, item.imageUrl)

            imageViewHomeTournament.load(context, item.imageUrl)
            imageViewHomeTournamentIcon.load(context, item.gameImageUrl)
            textViewHomeTournamentName.text = item.name
            item.registrationStartDate.toCalendar()?.let { date ->
                textViewHomeTournamentDate.text = date.time.myToString("dd.MM.yyyy")
            }
            /**
             * item.userJoinStatus
             * 0 -> Katil
             * 1 -> Katildin
             * 2 -> Beklemede (Onay Bekliyor)
             */
            if (item.userJoinStatus == 0) {
                item.registrationEndDate.toCalendar()?.let { date ->
                    if (date.time < Calendar.getInstance().time) {
                        buttonHomeTournamentsJoin.visibility = View.INVISIBLE
                    } else {
                        buttonHomeTournamentsJoin.visibility = View.VISIBLE
                        buttonHomeTournamentsJoin.text = context.getString(R.string.join)
                        buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.blue)
                    }
                }
            } else {
                if (item.cancelButtonActive == true) {
                    buttonHomeTournamentsJoin.visibility = View.VISIBLE
                    buttonHomeTournamentsJoin.setMyBackgroundTintList(context, R.color.tertiary_dark)
                    if (item.userJoinStatus == 1)
                        buttonHomeTournamentsJoin.text = context.getString(R.string.joined)
                    else if (item.userJoinStatus == 2)
                        buttonHomeTournamentsJoin.text = context.getString(R.string.waiting)
                } else buttonHomeTournamentsJoin.visibility = View.INVISIBLE
            }
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
            buttonHomeTournamentsJoin.setOnClickListener {
                onJoinTournamentClickListener?.invoke(item)
            }
        }
    }

}