package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_complete_import_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.SelectDialogButton


class CompleteImportWalletActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_complete_import_wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setToolbarTitle(R.string.complete_import_wallet_activity_title)
        setToolBarBackButton()

        nextHomeButton.setOnClickListener {
            if (!PinCodeHelper.isAvailable(this)) {
                showSecurityWarningDialog()
            } else {
                finishHome()
            }
        }

        nextLessonButton.setOnClickListener {
            startActivity(PrivateKeyStoreSupportActivity.createIntent(this))
            finish()
        }
    }

    private fun showSecurityWarningDialog() {
        val viewModel = RaccoonSelectViewModel(
                getString(R.string.com_ok),
                getString(R.string.com_cancel)
        )
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    when (item) {
                        SelectDialogButton.POSITIVE -> clickOK()
                        SelectDialogButton.NEGATIVE -> clickCancel()
                    }
                }
        RaccoonSelectDialog.createDialog(
                viewModel,
                getString(R.string.choose_start_security_fragment_dialog_title),
                getString(R.string.choose_start_security_fragment_dialog_message))
                .show(supportFragmentManager, RaccoonSelectDialog::class.java.toString())
    }

    private fun clickOK() {
        val viewModel = RaccoonSelectViewModel(
                getString(R.string.com_ok),
                getString(R.string.com_cancel)
        )
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    when (item) {
                        SelectDialogButton.POSITIVE -> clickOKSecondTime()
                        SelectDialogButton.NEGATIVE -> clickCancel()
                    }
                }
        RaccoonSelectDialog.createDialog(
                viewModel,
                getString(R.string.choose_start_security_fragment_dialog_title_second),
                getString(R.string.choose_start_security_fragment_dialog_message_second))
                .show(supportFragmentManager, RaccoonSelectDialog::class.java.toString())
    }

    private fun clickCancel() {
        // nothing to do
    }

    private fun clickOKSecondTime() {
        finishHome()
    }

    private fun finishHome() {
        val intent = MainActivity.createIntent(this, false)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, CompleteImportWalletActivity::class.java)
    }
}