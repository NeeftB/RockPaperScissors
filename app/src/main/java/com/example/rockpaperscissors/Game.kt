package com.example.rockpaperscissors

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class Game (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "computer_move")
    val computerMove: Int,

    @ColumnInfo(name = "player_move")
    val playerMove: Int,

    @ColumnInfo(name = "game_result")
    val gameResult: String
) {
    companion object {

        val MOVE_DRAWABLE_IDS = arrayOf(
            R.drawable.rock,
            R.drawable.paper,
            R.drawable.scissors
        )
    }
}