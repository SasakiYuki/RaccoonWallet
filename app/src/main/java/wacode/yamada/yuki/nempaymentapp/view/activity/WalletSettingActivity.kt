package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.view.fragment.WalletSettingFragment

class WalletSettingActivity : BaseFragmentActivity() {
    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    override fun initialFragment() = WalletSettingFragment.newInstance(getWalletId, isMultisig)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    private val getWalletId by lazy {
        intent.getLongExtra(KEY_WALLET_ID, 0L)
    }

    private val isMultisig by lazy {
        intent.getBooleanExtra(KEY_IS_MULTISIG, false)
    }

    companion object {
        private const val KEY_WALLET_ID = "key_wallet_id"
        private const val KEY_IS_MULTISIG = "key_is_multisig"

        fun getCallingIntent(context: Context, id: Long, isMultisig: Boolean): Intent {
            val intent = Intent(context, WalletSettingActivity::class.java)
            intent.putExtra(KEY_WALLET_ID, id)
            intent.putExtra(KEY_IS_MULTISIG, isMultisig)
            return intent
        }
    }
}