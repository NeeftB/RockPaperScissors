package com.example.rockpaperscissors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.game_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameActivity : AppCompatActivity() {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameRepository = GameRepository(this)

        initViews()
    }

    private fun initViews() {
        setStatistics()
        setListeners()
    }

    private fun setStatistics() {
        mainScope.launch {
            val wins = withContext(Dispatchers.IO) {
                gameRepository.getCountOf("won")
            }
            val lost = withContext(Dispatchers.IO) {
                gameRepository.getCountOf("lost")
            }
            val draw = withContext(Dispatchers.IO) {
                gameRepository.getCountOf("draw")
            }
            tvStatistics.text = getString(R.string.statistics, wins, draw, lost)
            }

    }

    private fun setListeners() {
        ivRock.setOnClickListener { gameTurn(0) }
        ivPaper.setOnClickListener { gameTurn(1) }
        ivScissors.setOnClickListener { gameTurn(2) }
    }

    // 0 = Rock, 1 = Paper, 2 = Scissors
    private fun gameTurn(playerMove: Int) {
        when (playerMove) {
            0 -> ivPlayerMove.setImageDrawable(this.getDrawable(R.drawable.rock))
            1 -> ivPlayerMove.setImageDrawable(this.getDrawable(R.drawable.paper))
            2 -> ivPlayerMove.setImageDrawable(this.getDrawable(R.drawable.scissors))
            else -> //Hier een snackbar maken
                return
        }
        val computerMove = computerMove()
        val result = checkGame(playerMove, computerMove)

        addDate(playerMove, computerMove, result)
        setStatistics()
    }

    private fun addDate(playerMove: Int, computerMove: Int, result: String){
        mainScope.launch {
            val game = Game(
                date = Date().toString(),
                playerMove = playerMove,
                computerMove = computerMove,
                gameResult = result
            )
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }

    private fun checkGame(playerMove: Int, computerMove: Int): String {
        //combine results to see who has won.
        // draw = 00, 11, 22
        // playerWin = 01, 12, 20
        // compWn = 02, 10, 21
        val result = computerMove.toString() + playerMove.toString()
        return if(result == "00" || result == "11" || result == "22"){
            tvResult.text = "It's a draw!"
            "draw"
        } else if(result == "01" || result == "12" || result == "20"){
            tvResult.text = "You have won!"
            "won"
        } else {
            tvResult.text = "Computer has won!"
            "lost"
        }
    }

    private fun computerMove(): Int {
        val randomInteger = (0..2).shuffled().first()
        when (randomInteger) {
            0 -> ivComputerMove.setImageDrawable(this.getDrawable(R.drawable.rock))
            1 -> ivComputerMove.setImageDrawable(this.getDrawable(R.drawable.paper))
            2 -> ivComputerMove.setImageDrawable(this.getDrawable(R.drawable.scissors))
        }

        return randomInteger
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_history_games -> {
                startGameHistoryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startGameHistoryActivity() {
        val intent = Intent(this, GameHistoryActivity::class.java)
        startActivity(intent)
    }
}
