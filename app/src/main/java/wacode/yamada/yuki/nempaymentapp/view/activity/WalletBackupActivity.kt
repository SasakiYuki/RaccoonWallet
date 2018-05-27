package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import wacode.yamada.yuki.nempaymentapp.view.fragment.WalletBackupWarningFragment

class WalletBackupActivity : BaseFragmentActivity() {
    override fun initialFragment() = WalletBackupWarningFragment.newInstance(getWalletId)

    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    private val getWalletId by lazy {
        intent.getLongExtra(INTENT_WALLET_ID, 0L)
    }

    companion object {
        private const val INTENT_WALLET_ID = "intent_wallet_id"
        fun getCallingIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, WalletBackupActivity::class.java)
            intent.putExtra(INTENT_WALLET_ID, id)
            return intent
        }
    }
}