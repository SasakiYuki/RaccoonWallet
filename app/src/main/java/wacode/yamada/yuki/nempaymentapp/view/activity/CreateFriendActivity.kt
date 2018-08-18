package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_friend.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendAddressFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendWalletFragment

class CreateFriendActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_create_friend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val list = ArrayList<BaseFragment>()
        list.add(CreateFriendWalletFragment.newInstance())
        list.add(CreateFriendAddressFragment.newInstance())

        val adapter = SimpleViewPagerAdapter(this, list, supportFragmentManager)
        createFriendViewPager.adapter = adapter
        createFriendTabLayout.setupWithViewPager(createFriendViewPager)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, CreateFriendActivity::class.java)
            return intent
        }
    }
}