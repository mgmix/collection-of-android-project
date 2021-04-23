package mgmix.dev.collection_of_android_project.calculator

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.room.Room
import mgmix.dev.collection_of_android_project.R
import mgmix.dev.collection_of_android_project.calculator.model.History
import org.w3c.dom.Text

class CalculatorActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy { findViewById(R.id.tv_expression) }
    private val resultTextView: TextView by lazy { findViewById(R.id.tv_result) }
    private val historyLayout: View by lazy { findViewById(R.id.historyLayout)}
    private val historyLinearLayout: LinearLayout by lazy { findViewById(R.id.historyLinearLayout) }

    private lateinit var db: AppDatabase

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        actionBar.apply { title = "Calculator" }

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClicked(view: View) {
        when (view.id) {
            R.id.btn0 -> numberButtonClicked("0")
            R.id.btn1 -> numberButtonClicked("1")
            R.id.btn2 -> numberButtonClicked("2")
            R.id.btn3 -> numberButtonClicked("3")
            R.id.btn4 -> numberButtonClicked("4")
            R.id.btn5 -> numberButtonClicked("5")
            R.id.btn6 -> numberButtonClicked("6")
            R.id.btn7 -> numberButtonClicked("7")
            R.id.btn8 -> numberButtonClicked("8")
            R.id.btn9 -> numberButtonClicked("9")
            R.id.btnMulti -> operatorButtonClicked("X")
            R.id.btnDivider -> operatorButtonClicked("/")
            R.id.btnPlus -> operatorButtonClicked("+")
            R.id.btnMinus -> operatorButtonClicked("-")
            R.id.btnModular -> operatorButtonClicked("%")
        }

    }

    private fun numberButtonClicked(number: String) {

        if (isOperator) {
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText = expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (number == "0" && expressionText.last().isEmpty()) {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)
        resultTextView.text = calculateExpression()
    }

    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()) return

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }

            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val span = SpannableStringBuilder(expressionTextView.text)
        span.setSpan(
                ForegroundColorSpan(getColor(R.color.green)),
                expressionTextView.text.length - 1,
                expressionTextView.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = span
        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(view: View) {
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) return
        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
        }

        if (expressionTexts.first().isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        resultTextView.text = ""
        expressionTextView.text = resultText

        isOperator = false
        hasOperator = false
    }

    fun clearButtonClicked(view: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")
        if (hasOperator.not() || expressionTexts.size != 3) {
            return " "
        } else if (expressionTexts.first().isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "X" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(view: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.tv_expression).text = it.expression
                    historyView.findViewById<TextView>(R.id.tv_result).text = " ${it.result}"
                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()

    }

    fun historyClearButtonClicked(view: View) {
        historyLinearLayout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }

    fun closeHistoryButtonClicked(view: View) {
        historyLayout.isVisible = false
    }
}

fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
