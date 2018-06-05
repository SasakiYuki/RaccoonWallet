package wacode.yamada.yuki.nempaymentapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentManager
import io.reactivex.android.schedulers.AndroidSchedulers
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmViewModel

object ReviewAppealUtils {
    const val KEY_REVIEW = "sp_key_review"
    const val REVIEW_APPEAL_DAYS = -3 // 三日経ったら表示する
    private const val SP_IS_ALREADY_SHOWN_REVIEW_DIALOG = "sp_is_already_shown_review_dialog"

    fun createReviewDialog(context: Context, supportFragmentManager: FragmentManager, viewModel: RaccoonConfirmViewModel) {
        val title = context.getString(R.string.about_activity_review_title)
        val message = context.getString(R.string.about_activity_review_message)
        val buttonText = context.getString(R.string.about_activity_review_button)
        viewModel.checkEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    viewModel.saveSPTwiceDisplay(context, KEY_REVIEW, !item)
                }
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    openPlayStore(context)
                }

        val dialog = RaccoonConfirmDialog.createDialog(
                viewModel,
                title,
                message,
                buttonText,
                viewModel.shouldTwiceDisplay(context, KEY_REVIEW)
        )

        dialog.show(supportFragmentManager, RaccoonConfirmDialog::class.java.toString())
    }

    fun openPlayStore(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName)))
        } catch (exception: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)))
        }
    }

    fun saveAlreadyShownReviewDialog(context: Context) = SharedPreferenceUtils[context, SP_IS_ALREADY_SHOWN_REVIEW_DIALOG, true]

    fun isAlreadyShownReviewDialog(context: Context) = SharedPreferenceUtils[context, SP_IS_ALREADY_SHOWN_REVIEW_DIALOG, false]
}