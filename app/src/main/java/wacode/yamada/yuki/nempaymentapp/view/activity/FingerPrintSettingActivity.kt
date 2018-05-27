package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import wacode.yamada.yuki.nempaymentapp.view.fragment.CompletedPinCodeSettingFragment

class FingerPrintSettingActivity : BaseFragmentActivity() {
    override fun initialFragment() = CompletedPinCodeSettingFragment.newInstance()
    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, FingerPrintSettingActivity::class.java)
            return intent
        }
    }
}
