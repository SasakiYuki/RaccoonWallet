package wacode.yamada.yuki.nempaymentapp.view

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.view_wallet_back_bar.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicro
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager

class WalletBackBarView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    private var nemBalance: String? = null
    private var isBalanceForeground: Boolean

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.view_wallet_back_bar, this)

        val attrs = context!!.obtainStyledAttributes(attrs, R.styleable.WalletBackBarView, defStyleAttr, 0)
        isBalanceForeground = attrs.getBoolean(R.styleable.WalletBackBarView_is_balance_foreground, false)

        fetchNemBalance()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setupViews()
        setupRxBus()
    }

    private fun setupViews() {
        if (isBalanceForeground) {
            fetchAndSetBalance()
        } else {
            setupWalletName()
        }

        fab.setOnClickListener {
            (context as AppCompatActivity).finish()
        }

        walletBarTextView.setOnClickListener {
            if (walletBarTextView.tag as WalletBarView.WalletBarType == WalletBarView.WalletBarType.NAME) {
                setupNemBalance(nemBalance)
            } else {
                setupWalletName()
            }
        }
    }

    private fun setupNemBalance(balance: String?) {
        walletBarTextView.tag = WalletBarView.WalletBarType.BALANCE

        walletBarTextView.text = if (balance.isNullOrEmpty()) {
            this@WalletBackBarView.context.getText(R.string.wallet_bar_balance_error)
        } else {
            balance!! + this@WalletBackBarView.context.getString(R.string.com_xem_uppercase)
        }
    }

    private fun setupWalletName() {
        walletBarTextView.tag = WalletBarView.WalletBarType.NAME

        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@WalletBackBarView.context) }
                    .await()

            walletBarTextView.text = if (wallet == null) {
                this@WalletBackBarView.context.getString(R.string.wallet_bar_name_error)
            } else {
                wallet.name
            }
        }
    }

    private fun setupRxBus() {
        RxBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        RxBusEvent.SELECT, RxBusEvent.RENAME -> {
                            setupWalletName()
                            fetchNemBalance()
                        }
                        RxBusEvent.REMOVE -> {
                            setupWalletName()
                            nemBalance = null
                        }
                    }
                }
    }

    private fun fetchNemBalance() {
        nemBalance = null

        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@WalletBackBarView.context) }
                    .await()

            NemCommons.getAccountInfo(wallet!!.address)
                    .subscribe({
                        nemBalance = it.account.balance.convertNEMFromMicroToDouble().toString()
                    }, { e ->
                        nemBalance = null
                        e.printStackTrace()
                    })
        }
    }

    private fun fetchAndSetBalance() {
        async(UI) {
            val wallet = bg { WalletManager.getSelectedWallet(this@WalletBackBarView.context) }
                    .await()

            NemCommons.getAccountInfo(wallet!!.address)
                    .subscribe({
                        val balance = it.account.balance.convertNEMFromMicro().toString()
                        setupNemBalance(balance)
                    }, { e ->
                        e.printStackTrace()
                        setupNemBalance(null)
                    })
        }
    }
}
