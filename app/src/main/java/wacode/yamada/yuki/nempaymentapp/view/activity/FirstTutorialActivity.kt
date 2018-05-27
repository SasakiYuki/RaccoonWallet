package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import kotlinx.android.synthetic.main.activity_first_tutorial.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils
import wacode.yamada.yuki.nempaymentapp.view.adapter.TutorialPagerAdapter

class FirstTutorialActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_first_tutorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewPager()
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            SharedPreferenceUtils.put(this, MainActivity.SP_IS_FIRST_RACCOON, false)
            startActivity(ChooseCreateOrScanWalletActivity.createIntent(this, true))
            finish()
        }
    }

    private fun setupViewPager() {
        val adapter = TutorialPagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        pagerIndicator.indicatorResource = R.drawable.indicator_white
        pagerIndicator.setCount(adapter.count)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                pagerIndicator.setCurrentPosition(position)
            }

            override fun onPageSelected(position: Int) {
                messageTextView.setText(getMessage(position))
                if (position == 2) {
                    button.visibility = View.VISIBLE
                } else {
                    button.visibility = View.GONE
                }
            }
        })
    }

    private fun getMessage(position: Int): Int {
        return when (position) {
            0 -> R.string.first_tutorial_message1
            1 -> R.string.first_tutorial_message2
            2 -> R.string.first_tutorial_message3
            else -> R.string.first_tutorial_message1
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, FirstTutorialActivity::class.java)
            return intent
        }
    }
}
