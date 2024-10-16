package com.nuevo.gameness.ui.pages.personal.tournaments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.databinding.ItemTournamentsBinding
import com.nuevo.gameness.ui.base.BaseRecyclerAdapter
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.myToString
import com.nuevo.gameness.utils.setMyBackgroundTintList
import com.nuevo.gameness.utils.toCalendar
import java.util.*

class TournamentsAdapter : BaseRecyclerAdapter<TournamentItem, ItemTournamentsBinding>() {

    var onJoinTournamentClickListener: ((item: TournamentItem) -> Unit)? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTournamentsBinding.inflate(inflater, parent, false)

    override fun bind(position: Int, item: TournamentItem, binding: ItemTournamentsBinding) {
        binding.apply {
            imageViewTournament.load(context, item.imageUrl)
            imageViewTournamentIcon.load(context, item.gameImageUrl)
            imageViewTournament3.visibility = View.GONE
            imageViewTournament2.visibility = View.GONE
            imageViewTournament1.visibility = View.GONE
            item.randomParticipantImages?.let { images ->
                for (index in images.indices) {
                    when(index) {
                        0 -> imageViewTournament1
                        1 -> imageViewTournament2
                        2 -> imageViewTournament3
                        else -> null
                    }?.apply {
                        load(context, images[index].imageUrl)
                        visibility = View.VISIBLE
                    }
                }
            }

            item.registrationStartDate.toCalendar()?.let { calendar ->
                textViewTournamentDate.apply {
                    text = calendar.time.myToString("MMMM dd")
                    append(" - ")
                    append(item.endDate.toCalendar()?.time.myToString("dd, yyyy"))
                }
            }
            textViewParticipantCount.text = context.getString(R.string.participant_count, item.currentParticipantCount)
            textViewTournamentName.text = item.name
            /**
             * item.userJoinStatus
             * 0 -> Katil
             * 1 -> Katildin
             * 2 -> Beklemede (Onay Bekliyor)
             */
            if (item.userJoinStatus == 0) {
                item.registrationEndDate.toCalendar()?.let { date ->
                    if (date.time < Calendar.getInstance().time) {
                        buttonTournamentJoin.visibility = View.INVISIBLE
                    } else {
                        buttonTournamentJoin.visibility = View.VISIBLE
                        buttonTournamentJoin.text = context.getString(R.string.join)
                        buttonTournamentJoin.setMyBackgroundTintList(context, R.color.blue)
                    }
                }
            } else {
                if (item.cancelButtonActive == true) {
                    buttonTournamentJoin.visibility = View.VISIBLE
                    buttonTournamentJoin.setMyBackgroundTintList(context, R.color.almost_black)
                    if (item.userJoinStatus == 1)
                        buttonTournamentJoin.text = context.getString(R.string.joined)
                    else if (item.userJoinStatus == 2)
                        buttonTournamentJoin.text = context.getString(R.string.waiting)
                } else buttonTournamentJoin.visibility = View.INVISIBLE
            }

            buttonTournamentJoin.setOnClickListener {
                onJoinTournamentClickListener?.invoke(item)
            }
            root.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
        }
    }

}