package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wallet_remove.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.RxBusProvider

class WalletRemoveActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_wallet_remove

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    fun setupViews() {
        setToolbarTitle(R.string.wallet_remove_activity_title)
        setToolBarBackButton()

        removeButton.setOnClickListener {
            showProgress()
            async(UI) {
                bg {
                    val wallet = NemPaymentApplication.database.walletDao().getById(getWalletId)
                    NemPaymentApplication.database.walletDao().delete(wallet)
                }.await()
                RxBusProvider.rxBus.send(RxBusEvent.REMOVE)

                hideProgress()
                finish()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private val getWalletId by lazy {
        intent.getLongExtra(INTENT_WALLET_ID, 0L)
    }

    companion object {
        private const val INTENT_WALLET_ID = "intent_wallet_id"

        fun getCallingIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, WalletRemoveActivity::class.java)
            intent.putExtra(INTENT_WALLET_ID, id)
            return intent
        }
    }
}