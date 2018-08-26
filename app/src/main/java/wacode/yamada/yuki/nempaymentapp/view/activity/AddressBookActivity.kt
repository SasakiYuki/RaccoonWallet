package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_address_book.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendInfoFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.FriendWalletFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.AddressBookViewModel
import javax.inject.Inject


class AddressBookActivity : BaseActivity(), HasSupportFragmentInjector, OnFriendDataChangeCallback {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddressBookViewModel

    private val friendId by lazy {
        intent.getLongExtra(INTENT_FRIEND_ID, 0)
    }

    override fun setLayout() = R.layout.activity_address_book

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddressBookViewModel::class.java)
        setupViewModelObserve()

        setupViewPager()
        setupButtons()

        viewModel.getFriendData(friendId)
    }

    override fun onFriendInfoChanged(friendInfo: FriendInfo) {
        friendNameTextView.text = friendInfo.lastName + " " + friendInfo.firstName
        friendNameKanaTextView.text = friendInfo.lastNameKana + " " + friendInfo.firstNameKana
        twitterAuthIcon.visibility = if (friendInfo.isTwitterAuth) View.VISIBLE else View.GONE
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@AddressBookActivity, Observer {
                it ?: return@Observer
                if (it) showProgress() else hideProgress()
            })

            friendIconLiveData.observe(this@AddressBookActivity, Observer {
                it ?: return@Observer

                Picasso.with(this@AddressBookActivity)
                        .load(it.iconPath)
                        .placeholder(R.mipmap.icon_default_profile)
                        .error(R.mipmap.icon_default_profile)
                        .into(circleImageView)
            })
        }
    }

    private fun setupViewPager() {
        val list = ArrayList<BaseFragment>()
        list.add(FriendWalletFragment.newInstance())
        list.add(FriendInfoFragment.newInstance(1))

        val adapter = SimpleViewPagerAdapter(this, list, supportFragmentManager)
        addressBookViewPager.adapter = adapter
        addressBookTabLayout.setupWithViewPager(addressBookViewPager)

        addressBookViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    FriendWalletFragment.PAGE_POSITION -> {
                        walletRootButton.visibility = View.VISIBLE
                        friendInfoRootButton.visibility = View.GONE
                    }
                    FriendInfoFragment.PAGE_POSITION -> {
                        walletRootButton.visibility = View.GONE
                        friendInfoRootButton.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setupButtons() {
        backImageView.setOnClickListener { finish() }
    }

    companion object {
        private const val INTENT_FRIEND_ID = "intent_friend_id"

        fun createIntent(context: Context, friendId: Long): Intent {
            val intent = Intent(context, AddressBookActivity::class.java)
            intent.putExtra(INTENT_FRIEND_ID, friendId)
            return intent
        }
    }
}

interface OnFriendDataChangeCallback {
    fun onFriendInfoChanged(friendInfo: FriendInfo)
}