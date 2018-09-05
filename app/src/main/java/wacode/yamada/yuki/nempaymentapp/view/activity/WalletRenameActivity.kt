package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_wallet_rename.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.isNotTextEmptyObservable
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent

class WalletRenameActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_wallet_rename

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        nameEditText.isNotTextEmptyObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    checkButton.setImageResource(if (item) R.mipmap.icon_check_green else R.mipmap.icon_check_gray)
                    checkButton.isEnabled = item
                })

        checkButton.setOnClickListener {
            showProgress()
            async(UI) {
                bg {
                    val wallet = NemPaymentApplication.database.walletDao().getById(getWalletId)
                    val newWallet = Wallet(
                            id = wallet.id,
                            salt = wallet.salt,
                            encryptedSecretKey = wallet.encryptedSecretKey,
                            publicKey = wallet.publicKey,
                            address = wallet.address,
                            name = nameEditText.text.toString())

                    NemPaymentApplication.database.walletDao().update(newWallet)
                }.await()

                RxBus.send(RxBusEvent.RENAME)

                hideProgress()
                finish()
            }
        }
    }

    private val getWalletId by lazy {
        intent.getLongExtra(INTENT_WALLET_ID, 0L)
    }

    companion object {
        private const val INTENT_WALLET_ID = "intent_wallet_id"

        fun getCallingIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, WalletRenameActivity::class.java)
            intent.putExtra(INTENT_WALLET_ID, id)
            return intent
        }
    }
}