package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import wacode.yamada.yuki.nempaymentapp.R

class AddressListActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_address_list

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressListActivity::class.java)
            return intent
        }
    }
}