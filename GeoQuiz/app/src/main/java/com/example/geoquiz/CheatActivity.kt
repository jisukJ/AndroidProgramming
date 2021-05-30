package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "CheatActivity"
const val EXTRA_ANSWER_SHOWN="com.example.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.example.android.geoquiz.answer_is_true"
private const val CHEAT_COUNTER = "com.example.android.geoquiz.cheat_counter"

class CheatActivity : AppCompatActivity() {
    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var apiTextView: TextView
    private lateinit var showAnswerButton: Button
    private var isAnswerShown =false
    private var cheatCounter=3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        isAnswerShown = savedInstanceState?.getBoolean(EXTRA_ANSWER_SHOWN,false) ?: false
        cheatCounter=savedInstanceState?.getInt(CHEAT_COUNTER,intent.getIntExtra(CHEAT_COUNTER,3))?:intent.getIntExtra(CHEAT_COUNTER,3)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        setAnswerShownResult()
        initView()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_ANSWER_SHOWN, isAnswerShown)
        outState.putInt(CHEAT_COUNTER, cheatCounter)
    }
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    private fun setAnswerShownResult(){
        val data=Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown)
            putExtra(CHEAT_COUNTER,cheatCounter)
        }
        setResult(Activity.RESULT_OK,data)
    }

    private fun initView() {
        answerTextView = findViewById(R.id.answer_text_view)
        apiTextView=findViewById(R.id.api_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setText(this.resources.getString(R.string.show_answer_button)+"("+cheatCounter.toString()+")")
        showAnswerButton.setOnClickListener {
            if (cheatCounter > 0) {
                val answerText = when {
                    answerIsTrue -> R.string.true_button
                    else -> R.string.false_button
                }
                --cheatCounter
                showAnswerButton.setText(this.resources.getString(R.string.show_answer_button)+"("+cheatCounter.toString()+")")
                answerTextView.setText(answerText)
                isAnswerShown = true
                setAnswerShownResult()
            }else{
                Toast.makeText(this,"커닝 가능 횟수 초과!",Toast.LENGTH_SHORT).show()
            }
        }
        apiTextView.setText("API Level "+Build.VERSION.SDK_INT.toString())
    }
}