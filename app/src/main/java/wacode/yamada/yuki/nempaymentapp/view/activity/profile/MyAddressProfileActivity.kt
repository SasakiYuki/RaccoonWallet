package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_my_address_profile.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.extentions.buildSpannableText
import wacode.yamada.yuki.nempaymentapp.extentions.setSpan

class MyAddressProfileActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_my_address_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        tabs.setupWithViewPager(viewpager)
        setOnClickListener()
    }
    private fun setOnClickListener() {
        bottomButton.setClickListener(View.OnClickListener { startActivity(ProfileAddressAddActivity.createIntent(this@MyAddressProfileActivity)) })
    }
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            toolbarTitle.apply {
                text = getString(R.string.my_address_profile_activity_title_initial)
            }.buildSpannableText {
                val targetTop = getString(R.string.my_address_profile_activity_title_initial_guest)
                val targetBottom = getString(R.string.my_address_profile_activity_title_initial_bottom_guest)
                it.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MyAddressProfileActivity, R.color.textBlack)), targetTop)
                        .setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MyAddressProfileActivity, R.color.textGrayDark)), targetBottom)
                        .setSpan(AbsoluteSizeSpan(20, true), targetTop)
                        .setSpan(AbsoluteSizeSpan(14, true), targetBottom)
            }
            setDisplayHomeAsUpEnabled(true)
        }
    }
    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, MyAddressProfileActivity::class.java)
            return intent
        }
    }
}