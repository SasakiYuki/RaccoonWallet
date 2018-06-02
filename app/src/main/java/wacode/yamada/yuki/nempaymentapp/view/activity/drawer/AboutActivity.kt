package wacode.yamada.yuki.nempaymentapp.view.activity.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_about.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmDialog
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
            if (viewModel.shouldTwiceDisplay(this, RaccoonConfirmViewModel.KEY_REVIEW)) {
                createReviewDialog(viewModel)
            } else {
                openPlayStore()
            }
        }
    }

    private fun createReviewDialog(viewModel: RaccoonConfirmViewModel) {
        val title = getString(R.string.about_activity_review_title)
        val message = getString(R.string.about_activity_review_message)
        val buttonText = getString(R.string.about_activity_review_button)
        viewModel.checkEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    viewModel.saveSPTwiceDisplay(this, RaccoonConfirmViewModel.KEY_REVIEW, item)
                }
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    openPlayStore()
                }

        val dialog = RaccoonConfirmDialog.createDialog(
                viewModel,
                title,
                message,
                buttonText,
                viewModel.shouldTwiceDisplay(this, RaccoonConfirmViewModel.KEY_REVIEW)
        )

        dialog.show(supportFragmentManager, RaccoonConfirmDialog::class.java.toString())
    }

    private fun openPlayStore() {
        try {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.packageName)))
        } catch (exception: android.content.ActivityNotFoundException) {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + this.packageName)))
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AboutActivity::class.java)
            return intent
        }
    }
}
