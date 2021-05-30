package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0
private const val CHEAT_COUNTER = "com.example.android.geoquiz.cheat_counter"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.changeCurrentIndex(currentIndex)
        initView()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.cheated[quizViewModel.currentIndex] =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.cheatCounter=
                data?.getIntExtra(CHEAT_COUNTER,quizViewModel.cheatCounter)?:quizViewModel.cheatCounter
        }
    }

    private fun initView() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        fun updateQuestion() {
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        }

        fun checkAnswer(userAnswer: Boolean) {
            val correctAnswer = quizViewModel.currentQuestionAnswer
            val messageResId = when {
                quizViewModel.cheated[quizViewModel.currentIndex] -> R.string.judgment_toast
                userAnswer == correctAnswer -> R.string.correct_toast
                else -> R.string.incorrect_toast
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            intent.putExtra(CHEAT_COUNTER,quizViewModel.cheatCounter)
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                val options =
                    ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }
        updateQuestion()

    }
}