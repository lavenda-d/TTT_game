package com.example.tictactoe

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private var currentPlayer = "X"
    private var gameActive = true
    private var playAgainstAI = false
    private val gameState = Array(3) { Array(3) { "" } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        val buttonPlayWithAI = findViewById<Button>(R.id.buttonPlayWithAI)
        val buttonPlayWithFriends = findViewById<Button>(R.id.buttonPlayWithFriends)

        buttonPlayWithAI.setOnClickListener {
            playAgainstAI = true
            resetGame() // Start a new game
        }

        buttonPlayWithFriends.setOnClickListener {
            playAgainstAI = false
            resetGame() // Start a new game
        }

        val buttonReset = findViewById<Button>(R.id.buttonReset)
        buttonReset.setOnClickListener {
            resetGame()
        }
    }

    private fun onCellClicked(button: Button, position: Int) {
        if (gameActive) {
            val row = position / 3
            val col = position % 3

            if (gameState[row][col] == "") {
                gameState[row][col] = currentPlayer
                button.text = currentPlayer
                button.setBackgroundColor(
                    if (currentPlayer == "X") {
                        0xFF228B22.toInt() // Forest Green
                    } else {
                        0xFFF4A460.toInt() // Sandy Brown
                    }
                )
                checkForWin()
                if (gameActive && playAgainstAI && currentPlayer == "X") {
                    currentPlayer = "O" // Switch to AI
                    aiMove()
                } else {
                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                }
            } else {
                Toast.makeText(this, "Cell already occupied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun aiMove() {
        // Simple AI: Find the first available position for O
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                if (gameState[row][col] == "") {
                    val button = findViewById<Button>(
                        resources.getIdentifier("button${row * 3 + col}", "id", packageName)
                    )

                    // Update game state
                    gameState[row][col] = "O"
                    button.text = "O"
                    button.setBackgroundColor(0xFFF4A460.toInt()) // Sandy Brown
                    checkForWin()
                    currentPlayer = "X" // Switch back to player
                    return
                }
            }
        }
    }

    private fun checkForWin() {
        val winningCombinations = arrayOf(
            arrayOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2)),
            arrayOf(intArrayOf(1, 0), intArrayOf(1, 1), intArrayOf(1, 2)),
            arrayOf(intArrayOf(2, 0), intArrayOf(2, 1), intArrayOf(2, 2)),
            arrayOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0)),
            arrayOf(intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(2, 1)),
            arrayOf(intArrayOf(0, 2), intArrayOf(1, 2), intArrayOf(2, 2)),
            arrayOf(intArrayOf(0, 0), intArrayOf(1, 1), intArrayOf(2, 2)),
            arrayOf(intArrayOf(0, 2), intArrayOf(1, 1), intArrayOf(2, 0))
        )

        for (combination in winningCombinations) {
            val a = gameState[combination[0][0]][combination[0][1]]
            val b = gameState[combination[1][0]][combination[1][1]]
            val c = gameState[combination[2][0]][combination[2][1]]

            if (a == b && b == c && a != "") {
                gameActive = false
                showWinnerDialog(a)
                return
            }
        }

        // Check for a draw
        if (gameState.flatten().none { it == "" }) {
            gameActive = false
            showDrawDialog()
        }
    }

    private fun showWinnerDialog(winner: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
        builder.setMessage("Player $winner wins!")
        builder.setPositiveButton("Play Again") { _, _ -> resetGame() }
        builder.setNegativeButton("Main Menu") { _, _ -> finish() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showDrawDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over!")
        builder.setMessage("It's a draw!")
        builder.setPositiveButton("Play Again") { _, _ -> resetGame() }
        builder.setNegativeButton("Main Menu") { _, _ -> finish() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun resetGame() {
        gameActive = true
        currentPlayer = "X"
        gameState.forEach { row -> row.fill("") }
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        // Reset all button texts and background colors
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
            button.setBackgroundColor(0xFFFFFFFF.toInt()) // Reset color to white
            button.setOnClickListener { onCellClicked(button, i) }
        }
    }
}