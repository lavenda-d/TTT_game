package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        val buttonNewGame = findViewById<Button>(R.id.buttonNewGame)
        val buttonHelp = findViewById<Button>(R.id.buttonHelp)
        val buttonExit = findViewById<Button>(R.id.buttonExit)

        buttonNewGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        buttonHelp.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        buttonExit.setOnClickListener {
            finish()
        }
    }
}