package wacode.yamada.yuki.nempaymentapp.view.activity.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_about.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.utils.ReviewAppealUtils
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmViewModel


class AboutActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_about
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.about_activity_title)

        setToolBarBackButton()
        setupPolicyTextView()
        setupOfficialSiteLink()
        setupDiscordLink()
        setupOssLicenceButton()
        setupReviewButton()
    }

    private fun setupPolicyTextView() {
        policyButton.setOnClickListener {
            val url = "https://raccoonwallet.com/tos_pp/"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }

    private fun setupOfficialSiteLink() {
        officialSiteButton.setOnClickListener {
            val url = "https://raccoonwallet.com/"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }

    private fun setupDiscordLink() {
        discordLinkButton.setOnClickListener {
            val url = "https://discord.gg/FrvjRzm"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }

    private fun setupOssLicenceButton() {
        ossLicenceButton.setOnClickListener {
            val intent = Intent(this, OssLicensesMenuActivity::class.java)
            intent.putExtra("title", getString(R.string.about_activity_official_licence))
            startActivity(intent)
        }
    }

    private fun setupReviewButton() {
        plzReviewLayout.setOnClickListener {
            val viewModel = RaccoonConfirmViewModel()
            if (viewModel.shouldTwiceDisplay(this, ReviewAppealUtils.KEY_REVIEW)) {
                ReviewAppealUtils.saveAlreadyShownReviewDialog(this)
                ReviewAppealUtils.createReviewDialog(this, supportFragmentManager, viewModel)
            } else {
                ReviewAppealUtils.openPlayStore(this)
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AboutActivity::class.java)
            return intent
        }
    }
}
