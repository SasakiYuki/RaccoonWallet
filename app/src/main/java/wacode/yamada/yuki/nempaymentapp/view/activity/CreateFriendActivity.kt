package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import android.view.View
import com.isseiaoki.simplecropview.CropImageView
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
        setupViewPager()
        setupSelectIconButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_DROP_IMAGE -> {
                    data?.let {
                        val uriString = it.getStringExtra(CropImageActivity.PARAM_INTENT_RESULT_URI)
                        val icon = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(uriString))
                        iconImageView.setImageBitmap(icon)
                        iconImageView.tag = uriString

                        selectIconRootView.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupSelectIconButton() {
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

    companion object {
        private const val REQUEST_CODE_DROP_IMAGE = 1000

        fun createIntent(context: Context): Intent {
            val intent = Intent(context, CreateFriendActivity::class.java)
            return intent
        }
    }
}