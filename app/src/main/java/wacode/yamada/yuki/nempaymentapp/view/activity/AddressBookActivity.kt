package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.isseiaoki.simplecropview.CropImageView
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_address_book.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.ProfileAddressAddActivity
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

        setupViewPager()
        setupButtons()
    }

    override fun onFriendInfoChanged(friendInfo: FriendInfo) {
        friendNameTextView.text = friendInfo.name
        friendNameRubyTextView.text = friendInfo.nameRuby
        twitterAuthIcon.visibility = if (friendInfo.isTwitterAuth) View.VISIBLE else View.GONE

        if (friendInfo.iconPath.isNotEmpty()) {
            Picasso.with(this@AddressBookActivity)
                    .load(friendInfo.iconPath)
                    .placeholder(R.mipmap.icon_default_profile)
                    .error(R.mipmap.icon_default_profile)
                    .into(circleImageView)
        }
    }

    override fun onFriendWalletChanged(walletSize: Int) {
        walletSizeText.text = walletSize.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ProfileAddressAddActivity.REQUEST_CODE -> {
                    val item = data?.getSerializableExtra(ProfileAddressAddActivity.INTENT_WALLET_INFO) as WalletInfo
                    val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.addressBookViewPager + ":" + FriendWalletFragment.PAGE_POSITION)

                    if (fragment != null) {
                        (fragment as FriendWalletFragment).onListItemChanged(item)
                    }
                }
                REQUEST_CODE_DROP_IMAGE -> {
                    data?.let {
                        val uriString = it.getStringExtra(CropImageActivity.PARAM_INTENT_RESULT_URI)
                        Picasso.with(this).load(uriString).into(circleImageView)
                        circleImageView.tag = uriString

                        selectIconRootView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupViewPager() {
        val list = ArrayList<BaseFragment>()
        list.add(FriendWalletFragment.newInstance(friendId))
        list.add(FriendInfoFragment.newInstance(friendId))

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
                        walletEditCompleteButton.visibility = View.GONE
                        selectIconRootView.visibility = View.GONE

                        val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.addressBookViewPager + ":" + FriendInfoFragment.PAGE_POSITION)
                        if (fragment != null) {
                            (fragment as FriendInfoFragment).changeDisplayFriendInfo()
                        }
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

        walletRootButton.setClickListener(View.OnClickListener {
            startActivityForResult(ProfileAddressAddActivity.createIntent(this, ProfileAddressAddActivity.ProfileAddressAddType.FriendWallet), ProfileAddressAddActivity.REQUEST_CODE)
        })

        friendInfoRootButton.setClickListener(View.OnClickListener {
            selectIconRootView.visibility = View.VISIBLE
            friendInfoRootButton.visibility = View.GONE
            walletEditCompleteButton.visibility = View.VISIBLE

            val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.addressBookViewPager + ":" + FriendInfoFragment.PAGE_POSITION)
            if (fragment != null) {
                (fragment as FriendInfoFragment).changeEditFriendInfo()
            }
        })

        walletEditCompleteButton.setClickListener(View.OnClickListener {
            updateFriendInfo()
        })

        selectIconRootView.setOnClickListener {
            startActivityForResult(CropImageActivity.createIntent(this, CropImageView.CropMode.CIRCLE_SQUARE), REQUEST_CODE_DROP_IMAGE)
        }
    }

    private fun updateFriendInfo() {
        val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.addressBookViewPager + ":" + FriendInfoFragment.PAGE_POSITION)
        if (fragment != null) {
            val friendInfoFragment = fragment as FriendInfoFragment
            friendInfoFragment.getAndCheckFriendInfo()?.let {
                val url = circleImageView.tag?.let { it as String } ?: run { "" }
                it.iconPath = url

                viewModel.updateFriendInfo(it)
            }

            selectIconRootView.visibility = View.GONE
            friendInfoRootButton.visibility = View.VISIBLE
            walletEditCompleteButton.visibility = View.GONE
            friendInfoFragment.changeDisplayFriendInfo()
        }
    }

    companion object {
        private const val REQUEST_CODE_DROP_IMAGE = 1000
        const val REQUEST_CODE_ADDRESS_BOOK = 109
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

    fun onFriendWalletChanged(walletSize: Int)
}