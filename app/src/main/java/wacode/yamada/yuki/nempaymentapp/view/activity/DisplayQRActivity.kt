package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_display_qr.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.convertMicroNEM
import wacode.yamada.yuki.nempaymentapp.extentions.generateQRCode
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import wacode.yamada.yuki.nempaymentapp.model.PaymentQRInnerEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.RxBusProvider
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import java.text.NumberFormat

class DisplayQRActivity : BaseActivity() {
    private val mainAmount by lazy {
        intent.getDoubleExtra(INTENT_MAIN_AMOUNT, 0.0)
    }
    private val formattedMainAmount by lazy {
        NumberFormat.getNumberInstance().format(mainAmount)
    }
    private val subAmount by lazy {
        intent.getDoubleExtra(INTENT_SUB_AMOUNT, 0.0)
    }
    private val formattedSubAmount by lazy {
        NumberFormat.getNumberInstance().format(subAmount)
    }
    private val calculatorMode by lazy {
        intent.getSerializableExtra(INTENT_CALCULATOR_MODE)
    }

    override fun setLayout() = R.layout.activity_display_qr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
        setupButton()
        getSelectedWallet()
        setupAmounts()
        setupRxBus()
    }

    private fun setupAmounts() {
        mainAmountText.text = (formattedMainAmount.toString() + if (calculatorMode == CalculatorActivity.CalculatorMode.MAIN_XEM) " " + getString(R.string.com_xem_uppercase) else " " + getString(R.string.com_jpy_uppercase))
        subAmountText.text = ("=" + formattedSubAmount.toString() + if (calculatorMode == CalculatorActivity.CalculatorMode.MAIN_XEM) " " + getString(R.string.com_jpy_uppercase) else " " + getString(R.string.com_xem_uppercase))
    }

    private fun setupRxBus() {
        RxBusProvider.rxBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        RxBusEvent.SELECT, RxBusEvent.RENAME -> {
                            getSelectedWallet()
                        }
                    }
                }
    }

    private fun setupButton() {
        toolbarCheckButton.setOnClickListener {
            val intent = MainActivity.createIntent(this, false)
            intent.flags = FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun getSelectedWallet() {
        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@DisplayQRActivity) }.await()
            wallet?.let {
                putQRImage(wallet)
                setupMyAddress(wallet)
            }
        }
    }

    private fun putQRImage(wallet: Wallet) {
        qrImageView.setImageBitmap(generateQRCode(generateQREntity(wallet)))
    }

    private fun generateQREntity(wallet: Wallet): PaymentQREntity {
        val childEntity = PaymentQRInnerEntity(wallet.address,
                getNemAmount().convertMicroNEM(),
                "",
                wallet.name)
        return PaymentQREntity(data = childEntity)
    }

    private fun setupMyAddress(wallet: Wallet) {
        myAddressTextView.text = wallet.displayAddress()
    }

    private fun getNemAmount() = if (calculatorMode == CalculatorActivity.CalculatorMode.MAIN_XEM) mainAmount else subAmount

    companion object {
        private const val INTENT_MAIN_AMOUNT = "intent_main_amount"
        private const val INTENT_SUB_AMOUNT = "intent_sub_amount"
        private const val INTENT_CALCULATOR_MODE = "intent_calculator_mode"

        fun createIntent(context: Context, mainAmount: Double, subAmount: Double, calculatorMode: CalculatorActivity.CalculatorMode): Intent {
            val intent = Intent(context, DisplayQRActivity::class.java)
            intent.putExtra(INTENT_MAIN_AMOUNT, mainAmount)
            intent.putExtra(INTENT_SUB_AMOUNT, subAmount)
            intent.putExtra(INTENT_CALCULATOR_MODE, calculatorMode)
            return intent
        }
    }
}
