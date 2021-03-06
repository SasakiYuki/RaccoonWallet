package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.isseiaoki.simplecropview.CropImageView
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_address_book.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.ProfileAddressAddActivity
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.custom_view.RaccoonDoubleMaterialButton
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendInfoFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendWalletFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.CreateAddressBookViewModel
import javax.inject.Inject


class CreateAddressBookActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CreateAddressBookViewModel

    override fun setLayout() = R.layout.activity_create_address_book

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateAddressBookViewModel::class.java)
        setupViewModelObserve()

        setupButtons()
        setupViewPager()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DROP_IMAGE -> {
                    data?.let {
                        val uriString = it.getStringExtra(CropImageActivity.PARAM_INTENT_RESULT_URI)
                        Picasso.with(this).load(uriString).into(circleImageView)
                        circleImageView.tag = uriString

                        selectIconRootView.visibility = View.VISIBLE
                    }
                }
                ProfileAddressAddActivity.REQUEST_CODE -> {
                    val item = data?.getSerializableExtra(ProfileAddressAddActivity.INTENT_WALLET_INFO) as WalletInfo
                    val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + CreateFriendWalletFragment.PAGE_POSITION)

                    if (fragment != null) {
                        (fragment as CreateFriendWalletFragment).onListItemChanged(item)
                    }
                }
            }
        }
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@CreateAddressBookActivity, Observer {
                it ?: return@Observer
                if (it) showProgress() else {
                    hideProgress()
                    finish()
                }
            })
        }
    }

    private fun setupButtons() {
        addressRootButton.setOnClickListener {
            insertFriendData()
        }

        addressRootButton.setClickListener(View.OnClickListener {
            insertFriendData()
        })

        twitterCooperationButton.setOnClickListener {
            //todo twitterAuthの追加
            showToast(R.string.com_coming_soon)
        }

        walletRootButton.setOnClickListener(object : RaccoonDoubleMaterialButton.OnDoubleButtonClickListener {
            override fun onLeftClick() {
                startActivityForResult(ProfileAddressAddActivity.createIntent(this@CreateAddressBookActivity, ProfileAddressAddActivity.ProfileAddressAddType.Other), ProfileAddressAddActivity.REQUEST_CODE)
            }

            override fun onRightClick() {
                insertFriendData()
            }
        })

        backImageView.setOnClickListener { finish() }

        circleImageView.setOnClickListener {
            startActivityForResult(CropImageActivity.createIntent(this, CropImageView.CropMode.CIRCLE_SQUARE), REQUEST_CODE_DROP_IMAGE)
        }
    }

    private fun setupViewPager() {
        val list = ArrayList<BaseFragment>()
        list.add(CreateFriendInfoFragment.newInstance())
        list.add(CreateFriendWalletFragment.newInstance())

        val adapter = SimpleViewPagerAdapter(this, list, supportFragmentManager)
        createAddressBookViewPager.adapter = adapter
        createAddressBookTabLayout.setupWithViewPager(createAddressBookViewPager)

        createAddressBookViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    CreateFriendWalletFragment.PAGE_POSITION -> {
                        walletRootButton.visibility = View.VISIBLE
                        addressRootButton.visibility = View.GONE
                    }
                    CreateFriendInfoFragment.PAGE_POSITION -> {
                        walletRootButton.visibility = View.GONE
                        addressRootButton.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun insertFriendData() {
        val fragment1 = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + CreateFriendWalletFragment.PAGE_POSITION)
        val fragment2 = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + CreateFriendInfoFragment.PAGE_POSITION)

        if (fragment1 != null && fragment2 != null) {
            val createFriendWalletFragment = fragment1 as CreateFriendWalletFragment
            val createFriendInfoFragment = fragment2 as CreateFriendInfoFragment

            val walletList = createFriendWalletFragment.walletList
            val friendInfo = createFriendInfoFragment.getAndCheckFriendInfo()

            if (friendInfo != null) {
                val uri = circleImageView.tag?.let { it as String } ?: run { "" }
                friendInfo.iconPath = uri

                viewModel.insertFriendData(friendInfo, walletList)
            } else {
                showToast(R.string.create_friend_input_error_message)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_DROP_IMAGE = 1000

        fun createIntent(context: Context): Intent {
            val intent = Intent(context, CreateAddressBookActivity::class.java)
            return intent
        }
    }
}