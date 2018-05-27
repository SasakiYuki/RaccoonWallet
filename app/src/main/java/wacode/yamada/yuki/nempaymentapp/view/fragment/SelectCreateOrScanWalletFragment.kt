package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_select_create_or_scan_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateWalletActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletImportActivity

class SelectCreateOrScanWalletFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_select_create_or_scan_wallet

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCreateButton()
    }

    private fun setupCreateButton() {
        createButton.setOnClickListener {
            if (PinCodeHelper.isAvailable(context)) {
                startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                        context, true, buttonPosition = NewCheckPinCodeActivity.ButtonPosition.LEFT), REQUEST_CODE_CREATE_WALLET)
            } else {
                startActivity(CreateWalletActivity.createIntent(context))
                activity.finish()
            }
        }

        loginButton.setOnClickListener {
            if (PinCodeHelper.isAvailable(context)) {
                startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                        context, true, buttonPosition = NewCheckPinCodeActivity.ButtonPosition.LEFT), REQUEST_CODE_IMPORT_WALLET)
            } else {
                startActivity(WalletImportActivity.getCallingIntent(context))
                activity.finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                PinCodeProvider.setPinCode(it.getByteArrayExtra(NewCheckPinCodeActivity.INTENT_PIN_CODE).toString(Charsets.UTF_8))

                when (requestCode) {
                    REQUEST_CODE_CREATE_WALLET -> startActivity(CreateWalletActivity.createIntent(context))
                    REQUEST_CODE_IMPORT_WALLET -> startActivity(WalletImportActivity.getCallingIntent(context))
                }
                activity.finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CREATE_WALLET = 1115
        private const val REQUEST_CODE_IMPORT_WALLET = 1116

        fun newInstance(): SelectCreateOrScanWalletFragment {
            val fragment = SelectCreateOrScanWalletFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.select_wallet_activity_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}