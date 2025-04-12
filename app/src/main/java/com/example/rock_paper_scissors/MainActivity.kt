package com.example.rock_paper_scissors

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rock_paper_scissors.enum.Choice

class MainActivity : AppCompatActivity() {

    //глобальные переменные
    private lateinit var playerChoice : Choice
    private lateinit var botChoice: Choice
    private lateinit var playerChoiceImage: ImageView
    private lateinit var botChoiceImage: ImageView
    private lateinit var resultText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultText = findViewById(R.id.resultText)
        botChoiceImage = findViewById(R.id.image_bot_choice)
        botChoiceImage = findViewById(R.id.image_player_choice)

    }


}