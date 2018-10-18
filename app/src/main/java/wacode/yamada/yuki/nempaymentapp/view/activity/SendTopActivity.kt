package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.SendTopFragment

class SendTopActivity : BaseFragmentActivity() {
    private val address by lazy {
        intent.getStringExtra(KEY_ADDRESS)
    }

    override fun setLayout() = BaseFragmentActivity.SIMPLE_FRAGMENT_ONLY_LAYOUT
    override fun initialFragment() = SendTopFragment.newInstance(address)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    companion object {
        private const val KEY_ADDRESS = "key_address"
        fun createIntent(context: Context, address: String): Intent {
            val intent = Intent(context, SendTopActivity::class.java)
            intent.putExtra(KEY_ADDRESS, address)
            return intent
        }
    }
}
