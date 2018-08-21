package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_address_book.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendInfoFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendWalletFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.AddressBookViewModel
import javax.inject.Inject


class AddressBookActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddressBookViewModel

    override fun setLayout() = R.layout.activity_address_book

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddressBookViewModel::class.java)
        setupViewModelObserve()

        setupViewPager()
        setupButtons()

        viewModel.getFriendData(1)
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@AddressBookActivity, Observer {
                it ?: return@Observer
                if (it) showProgress() else hideProgress()
            })

            friendInfoLiveData.observe(this@AddressBookActivity, Observer {
                it ?: return@Observer

                val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.addressBookViewPager + ":" + 1)
                if (fragment != null) {
                    val friendInfoFragment = fragment as FriendInfoFragment
                    friendInfoFragment.setupFriendInfo(it)
                }
                friendNameTextView.text = it.lastName + " " + it.firstName
                friendNameKanaTextView.text = it.lastNameKana + " " + it.firstNameKana
                twitterAuthIcon.visibility = if (it.isTwitterAuth) View.VISIBLE else View.GONE
            })

            friendIconLiveData.observe(this@AddressBookActivity, Observer {
                it ?: return@Observer

                Picasso.with(this@AddressBookActivity).load(it.iconPath).into(circleImageView)
            })
        }
    }

    private fun setupViewPager() {
        val list = ArrayList<BaseFragment>()
        list.add(FriendWalletFragment.newInstance())
        list.add(FriendInfoFragment.newInstance())

        val adapter = SimpleViewPagerAdapter(this, list, supportFragmentManager)
        addressBookViewPager.adapter = adapter
        addressBookTabLayout.setupWithViewPager(addressBookViewPager)
    }

    private fun setupButtons() {
        circleImageView.setOnClickListener { finish() }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressBookActivity::class.java)
            return intent
        }
    }
}