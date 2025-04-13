package com.example.rock_paper_scissors

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rock_paper_scissors.enum.Choice

class MainActivity : AppCompatActivity() {

    //глобальные переменные
    private var isPlayerChoiceMade: Boolean = false //сделал ли игрок выбор
    private var isBotChoiceMade: Boolean = false //сделал ли бот выбор
    private lateinit var playerChoice : Choice //выбор игрока
    private lateinit var botChoice: Choice //выбор компьютера
    private lateinit var playerChoiceImage: ImageView //изображение выбора игрока
    private lateinit var botChoiceImage: ImageView //изображение выбора компьютера
    private lateinit var drawResultText: TextView //текст, выводящий результат
    private lateinit var notDrawResultText: TextView //текст, выводящий результат


    //переменные кнопок
    private lateinit var button_start_game: Button
    private lateinit var button_rock: Button
    private lateinit var button_paper: Button
    private lateinit var button_spock: Button
    private lateinit var button_lizard: Button
    private lateinit var button_scissors: Button


    //переменные для вывода сообщения, если пользователь не сделал выбор
    val text = "Сделайте, пожалуйста, выбор!" //переменная с текстом об ошибке, если пользователь не сделал выбор
    var duration = Toast.LENGTH_SHORT //длительность показа сообщения


    //текстовые переменные
    private lateinit var result : String
    private lateinit var description : String


    //STATE
    private val KEY_PLAYER_CHOICE = "playerChoice"
    private val KEY_BOT_CHOICE = "botChoice"
    private val KEY_IS_PLAYER_CHOICE_MADE = "isPlayerChoiceMade"
    private val KEY_IS_BOT_CHOICE_MADE = "isBotChoiceMade"
    private val KEY_RESULT = "result"
    private val KEY_DESCRIPTION = "description"
    private val KEY_BUTTONS_DISABLED = "buttonsDisabled"


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean(KEY_IS_PLAYER_CHOICE_MADE, isPlayerChoiceMade)
            putBoolean(KEY_IS_BOT_CHOICE_MADE, isBotChoiceMade)
            if (isPlayerChoiceMade && ::playerChoice.isInitialized) {
                putString(KEY_PLAYER_CHOICE, playerChoice.name)
            }
            if (isBotChoiceMade && ::botChoice.isInitialized) {
                putString(KEY_BOT_CHOICE, botChoice.name)
            }
            if (::result.isInitialized) {
                putString(KEY_RESULT, result)
            }
            if (::description.isInitialized) {
                putString(KEY_DESCRIPTION, description)
            }
            putBoolean(KEY_BUTTONS_DISABLED, ::button_start_game.isInitialized && !button_start_game.isEnabled)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Инициализация всех View
        initViews()


        // Восстановление состояния
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }
    }


    private fun initViews() {
        button_start_game = findViewById(R.id.button_start_game)
        button_rock = findViewById(R.id.button_rock)
        button_paper = findViewById(R.id.button_paper)
        button_spock = findViewById(R.id.button_spock)
        button_lizard = findViewById(R.id.button_lizard)
        button_scissors = findViewById(R.id.button_scissors)

        drawResultText = findViewById(R.id.drawResultText)
        notDrawResultText = findViewById(R.id.notDrawResultText)
        botChoiceImage = findViewById(R.id.image_bot_choice)
        playerChoiceImage = findViewById(R.id.image_player_choice)
    }


    private fun restoreState(savedInstanceState: Bundle) {
        isPlayerChoiceMade = savedInstanceState.getBoolean(KEY_IS_PLAYER_CHOICE_MADE)
        isBotChoiceMade = savedInstanceState.getBoolean(KEY_IS_BOT_CHOICE_MADE)

        if (isPlayerChoiceMade) {
            playerChoice = Choice.valueOf(savedInstanceState.getString(KEY_PLAYER_CHOICE).toString())
            playerChoiceImage.setImageResource(playerChoice.imageResource)
        }

        if (isBotChoiceMade) {
            botChoice = Choice.valueOf(savedInstanceState.getString(KEY_BOT_CHOICE).toString())
            botChoiceImage.setImageResource(botChoice.imageResource)
        }

        result = savedInstanceState.getString(KEY_RESULT) ?: ""
        description = savedInstanceState.getString(KEY_DESCRIPTION) ?: ""

        if (result.isNotEmpty() && description.isNotEmpty()) {
            displayResult(result, description)
        }

        if (savedInstanceState.getBoolean(KEY_BUTTONS_DISABLED)) {
            allButtonsAreUnavailable()
        }
    }


    //метод, в котором определяется какой выбор сделал игрок(на какую кнопку нажал)
    fun playerMakesAChoice(view: View) {
        botChoiceImage.setImageResource(R.drawable.rules)
        drawResultText.visibility = View.INVISIBLE
        when (view.id) {
            R.id.button_rock -> playerChoice = Choice.ROCK
            R.id.button_paper -> playerChoice = Choice.PAPER
            R.id.button_scissors -> playerChoice = Choice.SCISSORS
            R.id.button_spock -> playerChoice = Choice.SPOCK
            R.id.button_lizard -> playerChoice = Choice.LIZARD
        }
        isPlayerChoiceMade = true //пометка, что игрок сделал выбор
        playerChoiceImage.setImageResource(playerChoice.imageResource) //показываем с помощью изображения выбор игрока
    }



    //метод обработки нажатия кнопки "Играть"
    fun startGame(view: View) {
        //если пользователь не сделал выбор
        if (!isPlayerChoiceMade) {
            Toast.makeText(applicationContext, text, duration).show() //сообщаем игроку, что нужно сделать выбор
            return
        }

        botChoice = Choice.entries.toTypedArray().random() //компьютер делает случайный выбор
        botChoiceImage.setImageResource(botChoice.imageResource) //выводим на экран выбор компьютера
        isBotChoiceMade = true

        result = determiningTheWinner() //узнаём результат игры
        description = descriptionOfTheVictory() //добавляем описание к результату
        displayResult(result, description) //показываем результат на экране

        //если кто-либо выиграл, то все кнопки становятся недоступны
        if (botChoice != playerChoice)
        {
            allButtonsAreUnavailable()
            return
        }

    }


    fun allButtonsAreUnavailable() {
        button_start_game.isEnabled = false
        button_start_game.visibility = View.INVISIBLE
        button_rock.isEnabled = false
        button_rock.visibility = View.INVISIBLE
        button_paper.isEnabled = false
        button_paper.visibility = View.INVISIBLE
        button_scissors.isEnabled = false
        button_scissors.visibility = View.INVISIBLE
        button_spock.isEnabled = false
        button_spock.visibility = View.INVISIBLE
        button_lizard.isEnabled = false
        button_lizard.visibility = View.INVISIBLE
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
            notDrawResultText.visibility = View.INVISIBLE
            notDrawResultText.text = "$result\n$description"
            notDrawResultText.visibility = View.VISIBLE
        }
        else {
            drawResultText.text = "$result\n$description"
            drawResultText.visibility = View.VISIBLE
        }
    }



}