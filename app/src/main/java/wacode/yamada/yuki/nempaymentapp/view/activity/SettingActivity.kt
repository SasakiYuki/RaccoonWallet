package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.fragment.SettingHomeFragment


class SettingActivity : BaseFragmentActivity() {
    override fun setLayout() = R.layout.activity_container

    override fun initialFragment() = SettingHomeFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBarBackButton()
    }

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}