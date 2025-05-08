package com.example.rock_paper_scissors

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rock_paper_scissors.databinding.ActivityMainBinding
import com.example.rock_paper_scissors.enum.Choice

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //глобальные переменные
    private var isPlayerChoiceMade: Boolean = false //сделал ли игрок выбор
    private lateinit var playerChoice : Choice //выбор игрока
    private lateinit var botChoice: Choice //выбор компьютера

    //переменные для вывода сообщения, если пользователь не сделал выбор
    val text = "Сделайте, пожалуйста, выбор!" //переменная с текстом об ошибке, если пользователь не сделал выбор
    var duration = Toast.LENGTH_SHORT //длительность показа сообщения

    //текстовые переменные
    private lateinit var result : String
    private lateinit var description : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //метод, в котором определяется какой выбор сделал игрок(на какую кнопку нажал)
    fun playerMakesAChoice(view: View) {
        binding.imageBotChoice.setImageResource(R.drawable.rules)
        binding.drawResultText.visibility = View.INVISIBLE
        when (view.id) {
            R.id.buttonRock -> playerChoice = Choice.ROCK
            R.id.buttonPaper -> playerChoice = Choice.PAPER
            R.id.buttonScissors -> playerChoice = Choice.SCISSORS
            R.id.buttonSpock -> playerChoice = Choice.SPOCK
            R.id.buttonLizard -> playerChoice = Choice.LIZARD
        }
        isPlayerChoiceMade = true //пометка, что игрок сделал выбор
        binding.imagePlayerChoice.setImageResource(playerChoice.imageResource) //показываем с помощью изображения выбор игрока
    }

    //метод обработки нажатия кнопки "Играть"
    fun startGame(view: View) {
        //если пользователь не сделал выбор
        if (!isPlayerChoiceMade) {
            Toast.makeText(applicationContext, text, duration).show() //сообщаем игроку, что нужно сделать выбор
            return
        }
        botChoice = Choice.entries.toTypedArray().random() //компьютер делает случайный выбор
        binding.imageBotChoice.setImageResource(botChoice.imageResource) //выводим на экран выбор компьютера

        result = determiningTheWinner() //узнаём результат игры
        description = descriptionOfTheVictory() //добавляем описание к результату
        displayResult(result, description) //показываем результат на экране

        //если кто-либо выиграл, то все кнопки становятся недоступны
        if (botChoice != playerChoice) allButtonsAreUnavailable()
    }

    fun allButtonsAreUnavailable() {
        binding.buttonStart.isEnabled = false
        binding.buttonStart.visibility = View.INVISIBLE
        binding.buttonRock.isEnabled = false
        binding.buttonRock.visibility = View.INVISIBLE
        binding.buttonPaper.isEnabled = false
        binding.buttonPaper.visibility = View.INVISIBLE
        binding.buttonScissors.isEnabled = false
        binding.buttonScissors.visibility = View.INVISIBLE
        binding.buttonSpock.isEnabled = false
        binding.buttonSpock.visibility = View.INVISIBLE
        binding.buttonLizard.isEnabled = false
        binding.buttonLizard.visibility = View.INVISIBLE
    }

    private fun determiningTheWinner(): String {
        if (botChoice == playerChoice) return "Ничья!"
        else when (playerChoice) {
            Choice.ROCK -> when (botChoice) {
                Choice.SCISSORS, Choice.LIZARD -> return "Вы выиграли!"
                else -> return "Вы проиграли!"
            }
            Choice.PAPER -> when (botChoice) {
                Choice.ROCK, Choice.SPOCK -> return "Вы выиграли!"
                else -> return "Вы проиграли!"
            }
            Choice.SCISSORS -> when (botChoice) {
                Choice.PAPER, Choice.LIZARD -> return "Вы выиграли!"
                else -> return "Вы проиграли!"
            }
            Choice.LIZARD -> when (botChoice) {
                Choice.PAPER, Choice.SPOCK -> return "Вы выиграли!"
                else -> return "Вы проиграли!"
            }
            Choice.SPOCK -> when (botChoice) {
                Choice.ROCK, Choice.SCISSORS -> return "Вы выиграли!"
                else -> return "Вы проиграли!"
            }
        }
    }

    private fun descriptionOfTheVictory():String {
        return when (playerChoice) {
            Choice.ROCK -> when (botChoice) {
                Choice.ROCK -> "Игра переигрывается, сделайте новый выбор."
                Choice.PAPER -> "Бумага заворачивает камень"
                Choice.SCISSORS -> "Камень разбивает ножницы"
                Choice.LIZARD -> "Камень давит ящерицу"
                Choice.SPOCK -> "Спок испаряет камень"
            }
            Choice.PAPER -> when (botChoice) {
                Choice.ROCK -> "Бумага заворачивает камень"
                Choice.PAPER -> "Игра переигрывается, сделайте новый выбор."
                Choice.SCISSORS -> "Ножницы режут бумагу"
                Choice.LIZARD -> "Ящерица ест бумагу"
                Choice.SPOCK -> "Бумага подставляет спока"
            }
            Choice.SCISSORS -> when (botChoice) {
                Choice.ROCK -> "Камень разбивает ножницы"
                Choice.PAPER -> "Ножницы режут бумагу"
                Choice.SCISSORS -> "Игра переигрывается, сделайте новый выбор."
                Choice.LIZARD -> "Ножницы отрезают голову ящерице"
                Choice.SPOCK -> "Спок ломает ножницы"
            }
            Choice.LIZARD -> when (botChoice) {
                Choice.ROCK -> "Камень давит ящерицу"
                Choice.PAPER -> "Ящерица ест бумагу"
                Choice.SCISSORS -> "Ножницы отрезают голову ящерице"
                Choice.LIZARD -> "Игра переигрывается, сделайте новый выбор."
                Choice.SPOCK -> "Ящерица травит спока"
            }
            Choice.SPOCK -> when (botChoice) {
                Choice.ROCK -> "Спок испаряет камень"
                Choice.PAPER -> "Бумага подставляет спока"
                Choice.SCISSORS -> "Спок ломает ножницы"
                Choice.LIZARD -> "Ящерица травит спока"
                Choice.SPOCK -> "Игра переигрывается, сделайте новый выбор."
            }
        }
    }

    //метод, выводящий на экран результат игры
    private fun displayResult(result: String, description: String) {
        // Центрируем текст если у кого-либо победа, чтобы текст был красиво отображён по центру
        if (botChoice != playerChoice)
        {
            binding.notDrawResultText.visibility = View.INVISIBLE
            binding.notDrawResultText.text = "$result\n$description"
            binding.notDrawResultText.visibility = View.VISIBLE
        }
        else {
            binding.drawResultText.text = "$result\n$description"
            binding.drawResultText.visibility = View.VISIBLE
        }
    }
}