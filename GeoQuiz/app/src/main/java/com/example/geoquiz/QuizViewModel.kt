package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"


class QuizViewModel : ViewModel() {
    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_ameriacs, true),
        Question(R.string.question_asia, true)
    )
    var currentIndex = 0
    var cheatCounter = 3
    var cheated = Array(questionBank.size) { false }
    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId

    fun changeCurrentIndex(index: Int) {
        if (index != currentIndex) {
            currentIndex = index
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}