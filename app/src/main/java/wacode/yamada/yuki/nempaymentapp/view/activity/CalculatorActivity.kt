package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_calclator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.contract.CalculationContract
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenter
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenterImpl
import wacode.yamada.yuki.nempaymentapp.types.CalculatorNumberType
import wacode.yamada.yuki.nempaymentapp.types.OperatorType
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.NemPricePreference
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.dialog.LoadingDialogFragment
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleCallbackDoubleInterface
import wacode.yamada.yuki.nempaymentapp.view.dialog.`interface`.SimpleNoInterface

class CalculatorActivity : BaseActivity(), CalculationContract.View {
    private var nemJPYPrice: Double? = null
    private var calculatorMode: CalculatorMode = CalculatorMode.MAIN_XEM

    override fun setLayout() = R.layout.activity_calclator

    override lateinit var presenter: CalculatorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolBar()
        setupButton()
        getNemPrice()
        setupUnit()
        presenter = CalculatorPresenterImpl(this)
    }

    private fun setupButton() {
        btnRightArrow.setOnClickListener {
            startActivity(DisplayQRActivity.createIntent(this,
                    txtDisplay.text.toString().toDouble(),
                    subAmount.text.toString().toDouble(),
                    calculatorMode))
        }
        modeChangeButton.setOnClickListener {
            calculatorMode = if (calculatorMode == CalculatorMode.MAIN_XEM) {
                CalculatorMode.MAIN_JPY
            } else {
                CalculatorMode.MAIN_XEM
            }
            setupUnit()
            onModeChanged()
        }
        btnMAX.setOnClickListener {
            val dialog = showLoadingDialogFragment()
            CoroutineScope(Dispatchers.Main).launch {

                val wallet = async(Dispatchers.IO) {
                    WalletManager.getSelectedWallet(this@CalculatorActivity)
                }.await()
                wallet?.let {
                    NemCommons.getAccountInfo(it.address)
                            .subscribe({ response ->
                                dialog.dismiss()
                                addString(response.account.balance.toString())
                            }, { e ->
                                e.printStackTrace()
                                this@CalculatorActivity.showToast(R.string.calculator_error)
                            })
                }
            }
        }
    }

    private fun setupToolBar() {
        toolbar.title = getString(R.string.calculator_title)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
    }

    private fun setupUnit() {
        if (calculatorMode == CalculatorMode.MAIN_XEM) {
            mainAmountUnit.text = getString(R.string.com_xem_uppercase)
            subAmountUnit.text = getString(R.string.com_jpy_uppercase)
        } else {
            mainAmountUnit.text = getString(R.string.com_jpy_uppercase)
            subAmountUnit.text = getString(R.string.com_xem_uppercase)
        }
    }

    private fun onModeChanged() {
        val subAmountString = subAmount.text.toString()
        addString(subAmountString)
    }

    private fun addString(amount: String) {
        presenter = CalculatorPresenterImpl(this)
        val charArray = amount.toCharArray()
        for (item in charArray) {
            (presenter as CalculatorPresenterImpl).typeNumberFromString(item.toString())
        }
    }

    private fun getNemPrice() {
        val dialog = showLoadingDialogFragment()

        NemPricePreference.getNemPriceFromZaif(object : SimpleCallbackDoubleInterface {
            override fun onCall(nemPrice: Double) {
                dialog.dismiss()
                nemJPYPrice = nemPrice
            }
        }, object : SimpleNoInterface {
            override fun onClickNo() {
                dialog.dismiss()
                this@CalculatorActivity.showToast(R.string.nem_converter_error_price)
            }
        })
    }

    private fun showLoadingDialogFragment(): LoadingDialogFragment {
        val dialog = LoadingDialogFragment()
        dialog.show(supportFragmentManager, LoadingDialogFragment::javaClass.toString())
        return dialog
    }

    fun onClickButton(v: View?) {
        try {
            when (v?.id) {
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
                else -> return
            }
        } catch (ex: Exception) {
            presenter = CalculatorPresenterImpl(this)
            txtDisplay.setText(R.string.calculator_error)
        }
    }

    override fun updateDisplay(value: String) {
        textDisplayRight.visibility = View.GONE
        txtDisplay.text = value
        setupSubAmount(value)
        if (value == "0") {
            btnMAX.visibility = View.VISIBLE
            btnRightArrow.visibility = View.GONE
        } else {
            btnMAX.visibility = View.GONE
            btnRightArrow.visibility = View.VISIBLE
        }
    }

    private fun setupSubAmount(valueString: String) {
        val valueDouble = valueString.toDouble()
        if (calculatorMode == CalculatorMode.MAIN_XEM) {
            nemJPYPrice?.let {
                subAmount.text = (valueDouble * it).toString()
            }
        } else {
            nemJPYPrice?.let {
                subAmount.text = (valueDouble / it).toString()
            }
        }
    }

    override fun updateRightDisplay(value: String) {
        textDisplayRight.visibility = View.VISIBLE
        textDisplayRight.text = value
        disableRightArrowButton()
    }

    override fun updateNextButton() {
        if (isEnableRightArrowButton()) {
            enableRightArrowButton()
        } else {
            disableRightArrowButton()
        }
    }

    private fun isEnableRightArrowButton(): Boolean {
        return if (textDisplayRight.visibility == View.VISIBLE) {
            false
        } else !txtSignals.text.isNotEmpty()
    }

    private fun enableRightArrowButton() {
        btnRightArrow.setBackgroundColor(ContextCompat.getColor(this, R.color.nemOrange))
        btnRightArrow.isEnabled = true
    }

    private fun disableRightArrowButton() {
        btnRightArrow.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_medium))
        btnRightArrow.isEnabled = false
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

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, CalculatorActivity::class.java)
        }
    }

    enum class CalculatorMode {
        MAIN_XEM,
        MAIN_JPY
    }
}
