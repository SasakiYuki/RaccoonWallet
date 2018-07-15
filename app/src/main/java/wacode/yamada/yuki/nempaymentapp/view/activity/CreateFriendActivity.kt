package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import wacode.yamada.yuki.nempaymentapp.R

class CreateFriendActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_create_friend

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, CreateFriendActivity::class.java)
            return intent
        }
    }
}