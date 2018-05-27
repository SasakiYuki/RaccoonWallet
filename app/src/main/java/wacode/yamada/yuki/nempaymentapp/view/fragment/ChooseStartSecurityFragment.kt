package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_choose_start_security.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.view.activity.OnCreateWalletPageChangeListener
import wacode.yamada.yuki.nempaymentapp.view.activity.PrivateKeyStoreSupportActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.SelectDialogButton


class ChooseStartSecurityFragment : BaseFragment() {
    private var listener: OnCreateWalletPageChangeListener? = null
    override fun layoutRes(): Int = R.layout.fragment_choose_start_security

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupButton()
    }

    private fun setupViews() {
        message.text = getMessage
    }

    private fun setupButton() {
        securityButton.setOnClickListener {
            startActivity(PrivateKeyStoreSupportActivity.createIntent(context))
            finish()
        }
        homeButton.setOnClickListener {
            if (!PinCodeHelper.isAvailable(context)) {
                showSecurityWarningDialog()
            } else {
                finish()
            }
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
                .show(activity.supportFragmentManager, RaccoonSelectDialog::class.java.toString())
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
                .show(activity.supportFragmentManager, RaccoonSelectDialog::class.java.toString())
    }

    private fun clickCancel() {
        // nothing to do
    }

    private fun clickOKSecondTime() {
        activity.finish()
    }

    private val getMessage by lazy {
        getString(arguments.getInt(KEY_MESSAGE))
    }

    companion object {
        private const val KEY_MESSAGE = "key_message"

        fun newInstance(listener: OnCreateWalletPageChangeListener, messageRes: Int): ChooseStartSecurityFragment {
            val fragment = ChooseStartSecurityFragment()
            val bundle = Bundle()
            bundle.putInt(BaseFragment.ARG_CONTENTS_NAME_ID, R.string.choose_start_security_activity_title)
            bundle.putInt(KEY_MESSAGE, messageRes)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}
