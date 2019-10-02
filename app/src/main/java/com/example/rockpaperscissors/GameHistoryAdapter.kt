package com.example.rockpaperscissors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.game_item.view.*

class GameHistoryAdapter(private val games: List<Game>): RecyclerView.Adapter<GameHistoryAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent,
                false)
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            itemView.tvDate.text = game.date
            itemView.tvResult.text = game.gameResult.capitalize()
            itemView.ivComputerMove.setImageDrawable(
                context.getDrawable(Game.MOVE_DRAWABLE_IDS[game.computerMove]))
            itemView.ivPlayerMove.setImageDrawable(
                context.getDrawable(Game.MOVE_DRAWABLE_IDS[game.playerMove]))
        }
    }
}