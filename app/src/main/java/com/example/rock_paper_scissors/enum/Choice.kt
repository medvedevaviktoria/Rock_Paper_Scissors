package com.example.rock_paper_scissors.enum

import com.example.rock_paper_scissors.R

enum class Choice(val imageResource: Int) {
    ROCK(R.drawable.choice_rock),
    PAPER(R.drawable.choice_paper),
    SCISSORS(R.drawable.choice_scissors),
    SPOCK(R.drawable.choice_spock),
    LIZARD(R.drawable.choice_lizard)
}