package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_tutorial_pin_code.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectDialog
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonSelectViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.SelectDialogButton
import wacode.yamada.yuki.nempaymentapp.view.fragment.WalletCreatedType

class TutorialPinCodeActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_tutorial_pin_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setToolbarTitle(R.string.tutorial_pin_code_fragment_title)
        setToolBarBackButton()

        button.setOnClickListener {
            if (PinCodeHelper.isAvailable(this)) {
                val viewModel = RaccoonSelectViewModel(getString(R.string.com_ok), getString(R.string.com_cancel))

                viewModel.clickEvent
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { item ->
                            if (item.equals(SelectDialogButton.POSITIVE)) {
                                startActivity(WalletCreatedActivity.createIntent(this, WalletCreatedType.NEWBIE))
                            }
                        }

                RaccoonSelectDialog.createDialog(viewModel,
                        getString(R.string.create_wallet_tutorial_dialog_title),
                        getString(R.string.create_wallet_tutorial_dialog_message))
                        .show(supportFragmentManager, "")
            } else {
                startActivityForResult(NewPinCodeSettingActivity.getCallingIntent(
                        this,
                        NewPinCodeSettingActivity.NextAction.BACK_FOR_RESULT), REQUEST_CODE_PIN_CODE_SETTING)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                when (requestCode) {
                    REQUEST_CODE_PIN_CODE_SETTING -> {
                        PinCodeProvider.setPinCode(it.getByteArrayExtra(NewPinCodeSettingActivity.INTENT_PIN_CODE).toString(Charsets.UTF_8))
                        startActivity(FingerPrintSettingActivity.createIntent(this))
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PIN_CODE_SETTING = 1117
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, TutorialPinCodeActivity::class.java)
            return intent
        }
    }
}
