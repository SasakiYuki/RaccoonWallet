package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.isseiaoki.simplecropview.CropImageView
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_create_address_book.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendAddressFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.CreateFriendWalletFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.AddressBookViewModel
import javax.inject.Inject


class CreateAddressBookActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddressBookViewModel

    override fun setLayout() = R.layout.activity_create_address_book

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddressBookViewModel::class.java)
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
                        Picasso.with(this).load(uriString).transform(CropCircleTransformation()).into(iconImageView)
                        iconImageView.tag = uriString

                        selectIconRootView.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@CreateAddressBookActivity, Observer {
                it ?: return@Observer
                if (it) showProgress() else hideProgress()
            })
        }
    }

    private fun setupButtons() {
        addressRootButton.setOnClickListener {
            //todo tap
            supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + 0)
        }

        twitterCooperationButton.setOnClickListener {
            //todo delete
            insertFriendData()
        }

        backImageView.setOnClickListener { finish() }

        iconImageView.setOnClickListener {
            startActivityForResult(CropImageActivity.createIntent(this, CropImageView.CropMode.CIRCLE_SQUARE), REQUEST_CODE_DROP_IMAGE)
        }
    }

    private fun setupViewPager() {
        val list = ArrayList<BaseFragment>()
        list.add(CreateFriendWalletFragment.newInstance())
        list.add(CreateFriendAddressFragment.newInstance())

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
                    0 -> {
                        walletRootButton.visibility = View.VISIBLE
                        addressRootButton.visibility = View.GONE
                    }
                    1 -> {
                        walletRootButton.visibility = View.GONE
                        addressRootButton.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun insertFriendData() {
        val fragment1 = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + 0)
        val fragment2 = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createAddressBookViewPager + ":" + 1)

        if (fragment1 != null && fragment2 != null) {
            val createFriendWalletFragment = fragment1 as CreateFriendWalletFragment
            val createFriendInfoFragment = fragment2 as CreateFriendAddressFragment

            val friendInfo = createFriendInfoFragment.getAndCheckFriendInfo()

            friendInfo?.let { friendInfo ->
                val uriString = iconImageView.tag
                val uri = uriString?.let { Uri.parse(it.toString()) } ?: run { null }

                viewModel.insertFriendData(contentResolver, uri, friendInfo)
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