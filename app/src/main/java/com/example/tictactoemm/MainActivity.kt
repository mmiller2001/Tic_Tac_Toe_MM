package com.example.tictactoemm

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import com.example.tictactoemm.R
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private var player1Turn = true
    private var roundCount = 0
    private var player1Points = 0
    private var player2Points = 0
    private var textViewPlayer1: TextView? = null
    private var textViewPlayer2: TextView? = null
    var player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewPlayer1 = findViewById(R.id.text_view_p1)
        textViewPlayer2 = findViewById(R.id.text_view_p2)
        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "button_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }
        val buttonReset = findViewById<Button>(R.id.button_reset)
        buttonReset.setOnClickListener { resetGame() }
    }

    override fun onClick(v: View) {
        if ((v as Button).text.toString() != "") {
            return
        }
        if (player1Turn) {
            v.text = "♚"
            v.setBackgroundColor(Color.BLACK)
        } else {
            v.text = "♜"
            v.setBackgroundColor(Color.RED)
        }
        roundCount++
        if (checkForWin()) {
            if (player1Turn) {
                waitingPlayer1(v)
                //player1Wins();
            } else {
                waitingPlayer2(v)
                //player2Wins();
            }
        } else if (roundCount == 9) {
            waitingTie(v)
            //draw();
        } else {
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "") {
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "") {
                return true
            }
        }
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "") {
            return true
        }
        return if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != "") {
            true
        } else false
    }

    private val mhandler = Handler()
    fun waitingPlayer1(v: View?) {
        mhandler.postDelayed(mToastRunnable, 3000)
    }

    fun waitingPlayer2(v: View?) {
        mhandler.postDelayed(mToastRunnable2, 3000)
    }

    fun waitingTie(v: View?) {
        mhandler.postDelayed(mToastRunnable3, 3000)
    }

    private val mToastRunnable = Runnable { player1Wins() }
    private val mToastRunnable2 = Runnable { player2Wins() }
    private val mToastRunnable3 = Runnable { draw() }
    private fun player1Wins() {
        player1Points++
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show()
        player = MediaPlayer.create(this, R.raw.erenjaegar)
        player?.start()
        updatePointsText()
        resetBoard()
    }

    private fun player2Wins() {
        player2Points++
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show()
        player = MediaPlayer.create(this, R.raw.oioioi)
        player?.start()
        updatePointsText()
        resetBoard()
    }

    private fun draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        player = MediaPlayer.create(this, R.raw.fail)
        player?.start()
        resetBoard()
    }

    private fun updatePointsText() {
        textViewPlayer1!!.text = "Player 1: $player1Points"
        textViewPlayer2!!.text = "Player 2: $player2Points"
    }

    private fun resetBoard() {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.text = ""
                buttons[i][j]!!.setBackgroundColor(color)
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        updatePointsText()
        resetBoard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Points", player1Points)
        outState.putInt("player2Points", player2Points)
        outState.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
}