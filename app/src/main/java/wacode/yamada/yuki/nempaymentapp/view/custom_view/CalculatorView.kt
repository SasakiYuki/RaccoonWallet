package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_calculator.view.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.contract.CalculationContract
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenter
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenterImpl
import wacode.yamada.yuki.nempaymentapp.types.CalculatorNumberType
import wacode.yamada.yuki.nempaymentapp.types.OperatorType

class CalculatorView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr), CalculationContract.View {
    override lateinit var presenter: CalculatorPresenter
    private var calculatorListener: OnClickCalculator? = null

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.view_calculator, this)
        presenter = CalculatorPresenterImpl(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupButtons()
        setupRightButton()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnZero).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnOne).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnTwo).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnThree).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnFive).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnFour).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnSix).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnSeven).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnEight).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnNine).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnDivide).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnDot).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnBack).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnCE).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnEquals).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnMinus).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnDivide).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnMultiply).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.btnPlus).setOnClickListener(onClickListener)
    }


    private val onClickListener = OnClickListener { p0 ->
        try {
            when (p0?.id) {
                R.id.btnZero -> presenter.typeNumber(CalculatorNumberType.ZERO)
                R.id.btnOne -> presenter.typeNumber(CalculatorNumberType.ONE)
                R.id.btnTwo -> presenter.typeNumber(CalculatorNumberType.TWO)
                R.id.btnThree -> presenter.typeNumber(CalculatorNumberType.THREE)
                R.id.btnFour -> presenter.typeNumber(CalculatorNumberType.FOUR)
                R.id.btnFive -> presenter.typeNumber(CalculatorNumberType.FIVE)
                R.id.btnSix -> presenter.typeNumber(CalculatorNumberType.SIX)
                R.id.btnSeven -> presenter.typeNumber(CalculatorNumberType.SEVEN)
                R.id.btnEight -> presenter.typeNumber(CalculatorNumberType.EIGHT)
                R.id.btnNine -> presenter.typeNumber(CalculatorNumberType.NINE)
                R.id.btnDot -> presenter.typeDot()
                R.id.btnCE -> presenter.ce()
                R.id.btnBack -> presenter.backspace()
                R.id.btnEquals -> presenter.typeEquals()
                R.id.btnPlus -> presenter.add()
                R.id.btnMinus -> presenter.minus()
                R.id.btnDivide -> presenter.divide()
                R.id.btnMultiply -> presenter.multiply()
                else -> return@OnClickListener
            }
        } catch (ex: Exception) {
            presenter = CalculatorPresenterImpl(this@CalculatorView)
            txtDisplay.setText(R.string.calculator_error)
        }
    }

    private fun setupRightButton() {
        btnRightArrow.setOnClickListener {
            calculatorListener?.onClickRight(txtDisplay.text.toString().toDouble())
        }
    }

    override fun updateDisplay(value: String) {
        textDisplayRight.visibility = View.GONE
        txtDisplay.text = value
        if (value == "0") {
            btnMAX.visibility = View.VISIBLE
            btnRightArrow.visibility = View.GONE
            calculatorListener?.onPutZERO()
        } else {
            btnMAX.visibility = View.GONE
            btnRightArrow.visibility = View.VISIBLE
            calculatorListener?.onPutNotZERO()
        }
    }

    override fun updateOperation(operation: OperatorType) {
        when (operation) {
            OperatorType.NONE -> txtSignals.text = ""
            OperatorType.ADDITION -> txtSignals.setText(R.string.calculator_plus_sign)
            OperatorType.SUBTRACTION -> txtSignals.setText(R.string.calculator_minus_sign)
            OperatorType.MULTIPLICATION -> txtSignals.setText(R.string.calculator_multiplication_sign)
            OperatorType.DIVISION -> txtSignals.setText(R.string.calculator_division_sign)
        }
        disableRightArrowButton()
    }

    override fun finish() {
    }

    private fun isEnableRightArrowButton(): Boolean {
        return if (textDisplayRight.visibility == View.VISIBLE) {
            false
        } else !txtSignals.text.isNotEmpty()
    }

    override fun updateRightDisplay(value: String) {
        textDisplayRight.visibility = View.VISIBLE
        textDisplayRight.text = value
        disableRightArrowButton()
    }

    private fun enableRightArrowButton() {
        btnRightArrow.setBackgroundColor(ContextCompat.getColor(context, R.color.nemOrange))
        btnRightArrow.isEnabled = true
    }

    private fun disableRightArrowButton() {
        btnRightArrow.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_medium))
        btnRightArrow.isEnabled = false
    }

    override fun updateNextButton() {
        if (isEnableRightArrowButton()) {
            enableRightArrowButton()
        } else {
            disableRightArrowButton()
        }
    }

    fun setCalculatorListener(onClickCalculator: OnClickCalculator) {
        calculatorListener = onClickCalculator
    }
}

public interface OnClickCalculator {
    public fun onClickRight(amount: Double)
    fun onPutZERO()
    fun onPutNotZERO()
}
