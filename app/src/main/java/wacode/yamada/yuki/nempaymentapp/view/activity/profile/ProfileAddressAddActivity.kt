package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity

class ProfileAddressAddActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_profile_address_add
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setToolBarBackButton()
        setTitle(R.string.profile_address_add_activity_title)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, ProfileAddressAddActivity::class.java)
            return intent
        }
    }
}
