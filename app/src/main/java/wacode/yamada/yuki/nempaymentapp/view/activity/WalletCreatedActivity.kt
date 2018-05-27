package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import wacode.yamada.yuki.nempaymentapp.view.fragment.WalletCreatedFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.WalletCreatedType

class WalletCreatedActivity : BaseFragmentActivity() {
    override fun initialFragment() = WalletCreatedFragment.newInstance(intent.getSerializableExtra(KEY_SCREEN_TYPE) as WalletCreatedType)
    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    companion object {
        private const val KEY_SCREEN_TYPE = "key_screen_type"
        fun createIntent(context: Context, type: WalletCreatedType): Intent {
            val intent = Intent(context, WalletCreatedActivity::class.java)
            intent.putExtra(KEY_SCREEN_TYPE, type)
            return intent
        }
    }
}
