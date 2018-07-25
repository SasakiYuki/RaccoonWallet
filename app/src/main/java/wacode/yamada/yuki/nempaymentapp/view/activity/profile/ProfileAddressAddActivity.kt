package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import wacode.yamada.yuki.nempaymentapp.R

class ProfileAddressAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_address_add)
    }

    companion object {
        fun createIntent(context:Context): Intent {
            val intent = Intent(context,ProfileAddressAddActivity::class.java)
            return intent
        }
    }
}
