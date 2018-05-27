package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_calculator_title.*
import kotlinx.android.synthetic.main.view_multi_calculator.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.contract.CalculationContract
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenter
import wacode.yamada.yuki.nempaymentapp.presenter.CalculatorPresenterImpl
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem
import wacode.yamada.yuki.nempaymentapp.types.CalculatorNumberType
import wacode.yamada.yuki.nempaymentapp.types.OperatorType
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.adapter.CalculatorPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.dialog.LoadingDialogFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.send.CalculatorTitleFragment


class MultiCalculatorView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr), CalculationContract.View {
    override lateinit var presenter: CalculatorPresenter
    private var calculatorListener: OnClickMultiCalculator? = null
    lateinit var pagerAdapter: CalculatorPagerAdapter
    private val calculatedPair: HashMap<String, String> = HashMap()

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    private var currentTxtDisplay: TextView? = null
    private var currentTxtSignals: TextView? = null
    private var currentTextDisplayRight: TextView? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        View.inflate(context, R.layout.view_multi_calculator, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        pagerAdapter = CalculatorPagerAdapter((context as AppCompatActivity).supportFragmentManager, ArrayList<MosaicItem>())
        pagerAdapter.add(MosaicItem.createNEMXEMItem())
        setupButtons()
        setupRightButton()
        setupMaxButton()
        setupViewPager()
        presenter = CalculatorPresenterImpl(this)
    }

    fun setupViewPager() {
        wrapViewPager.adapter = pagerAdapter
        pagerIndicator.setCount(pagerAdapter.count)
        wrapViewPager.addOnPageChangeListener(wrapViewPagerPageChangeListener)
    }

    val wrapViewPagerPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    presenter = CalculatorPresenterImpl(this@MultiCalculatorView)
                    setupCurrentTexts(position)
                    setupAmount(position)
                    pagerIndicator.setCurrentPosition(position)
                }
            }

    fun resetCalculatorPresenterImp() {
        presenter = CalculatorPresenterImpl(this@MultiCalculatorView)
    }

    fun setupCurrentTexts(position: Int) {
        if (pagerAdapter.fragments.size > 0) {
            val fragment = pagerAdapter.getCurrentFragment(position)
            if (fragment != null && fragment is CalculatorTitleFragment) {
                currentTxtDisplay = fragment.txtDisplay
                currentTxtSignals = fragment.txtSignals
                currentTextDisplayRight = fragment.textDisplayRight
            }
        }
    }

    fun setupAmount(position: Int) {
        if (pagerAdapter.getItem(position) != null) {
            val amount = calculatedPair[pagerAdapter.getItem(position).getFullName()]
            amount?.let {
                val charArray = it.toCharArray()
                for (item in charArray) {
                    (presenter as CalculatorPresenterImpl).typeNumberFromString(item.toString())
                }
            }
        }
    }

    private fun setAmountFromString(amount: String) {
        val charArray = amount.toCharArray()
        for (item in charArray) {
            (presenter as CalculatorPresenterImpl).typeNumberFromString(item.toString())
        }
    }

    fun getAmount(fullName: String) = calculatedPair[fullName]

    fun removeItem(mosaicItem: MosaicItem) {
        calculatedPair.remove(mosaicItem.getFullName())
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
                R.id.btnCE -> ce()
                R.id.btnBack -> presenter.backspace()
                R.id.btnEquals -> presenter.typeEquals()
                R.id.btnPlus -> presenter.add()
                R.id.btnMinus -> presenter.minus()
                R.id.btnDivide -> presenter.divide()
                R.id.btnMultiply -> presenter.multiply()
                else -> return@OnClickListener
            }
        } catch (ex: Exception) {
            presenter = CalculatorPresenterImpl(this)
            currentTxtDisplay?.setText(R.string.calculator_error)
        }
    }

    private fun ce() {
        presenter.ce()
        calculatedPair.remove(pagerAdapter.getItem(wrapViewPager.currentItem).getFullName())
    }

    private fun setupRightButton() {
        btnRightArrow.setOnClickListener {
            calculatorListener?.onClickRight(calculatedPair)
        }
    }

    private fun setupMaxButton() {
        btnMAX.setOnClickListener {
            val item = pagerAdapter.getItem(wrapViewPager.currentItem)
            if (item.isNEMXEMItem()) {
                getAccountInfoAndSetAmount()
            } else {
                setAmountFromString(item.getQuantity())
            }
        }
    }

    private fun getAccountInfoAndSetAmount() {
        async(UI) {
            val dialog = showLoadingDialogFragment()
            val wallet = bg { WalletManager.getSelectedWallet(context = getContext()) }
                    .await()
            compositeDisposable.add(
                    NemCommons.getAccountInfo(wallet!!.address)
                            .subscribe({ it ->
                                dialog.dismiss()
                                setAmountFromString(it.account.balance.convertNEMFromMicroToDouble().toString())
                            }, { e ->
                                dialog.dismiss()
                                getContext().showToast(R.string.enter_send_fragment_xem_amount_error)
                            }))
        }
    }

    private fun showLoadingDialogFragment(): LoadingDialogFragment {
        val dialog = LoadingDialogFragment()
        dialog.show((context as AppCompatActivity).supportFragmentManager, LoadingDialogFragment::javaClass.toString())
        return dialog
    }

    override fun updateDisplay(value: String) {
        if (pagerAdapter.fragments.size > 0 && value != "0") {
            val item = pagerAdapter.getItem(wrapViewPager.currentItem)
            calculatedPair.put(item.getFullName(), value)
        }
        currentTextDisplayRight?.visibility = View.GONE
        if (currentTxtDisplay == null) {
            setupCurrentTexts(wrapViewPager.currentItem)
        }
        currentTxtDisplay?.text = value
        if (value == "0") {
            btnMAX.visibility = View.VISIBLE
            btnRightArrow.visibility = View.GONE
            if (pagerAdapter.fragments.size > 0) {
                val item = pagerAdapter.getItem(wrapViewPager.currentItem)
                calculatorListener?.onPutZERO(item)
            }
        } else {
            btnMAX.visibility = View.GONE
            btnRightArrow.visibility = View.VISIBLE
            if (pagerAdapter.fragments.size > 0) {
                val item = pagerAdapter.getItem(wrapViewPager.currentItem)
                calculatorListener?.onPutNotZERO(item)
            }
        }
    }

    override fun updateOperation(operation: OperatorType) {
        currentTxtSignals?.let {
            when (operation) {
                OperatorType.NONE -> it.text = ""
                OperatorType.ADDITION -> it.setText(R.string.calculator_plus_sign)
                OperatorType.SUBTRACTION -> it.setText(R.string.calculator_minus_sign)
                OperatorType.MULTIPLICATION -> it.setText(R.string.calculator_multiplication_sign)
                OperatorType.DIVISION -> it.setText(R.string.calculator_division_sign)
            }
        }
        disableRightArrowButton()
    }

    override fun finish() {
    }

    private fun isEnableRightArrowButton(): Boolean {
        return if (currentTextDisplayRight != null && currentTxtSignals != null) {
            if (currentTextDisplayRight!!.visibility == View.VISIBLE) {
                false
            } else !currentTxtSignals!!.text.isNotEmpty()
        } else {
            false
        }
    }

    override fun updateRightDisplay(value: String) {
        currentTextDisplayRight?.let {
            it.visibility = View.VISIBLE
            it.text = value
        }
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

    fun resetCurrentTexts() {
        currentTxtDisplay = null
        currentTxtSignals = null
        currentTextDisplayRight = null
    }

    fun setCalculatorListener(onClickCalculator: OnClickMultiCalculator) {
        calculatorListener = onClickCalculator
    }
}

interface OnClickMultiCalculator {
    fun onClickRight(hashMap: HashMap<String, String>)
    fun onPutZERO(mosaicItem: MosaicItem)
    fun onPutNotZERO(mosaicItem: MosaicItem)
}
