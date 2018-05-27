package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_wallet_setting.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletBackupActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletRemoveActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletRenameActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.ReceiveFragment

class WalletSettingFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_wallet_setting

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        walletType.text = if (isMultisig) {
            getString(R.string.wallet_setting_type_multisig)
        } else {
            getString(R.string.wallet_setting_type_standard)
        }

        walletDetail.setOnClickListener {
            replaceFragment(WalletDetailFragment.newInstance(getWalletId), true)
        }

        walletAddress.setOnClickListener {
            replaceFragment(ReceiveFragment.newInstance(getWalletId, R.string.wallet_setting_address_title), true)
        }

        walletBackup.setOnClickListener {
            if (PinCodeHelper.isAvailable(context)) {
                startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                        context = context,
                        isDisplayFingerprint = true,
                        buttonPosition = NewCheckPinCodeActivity.ButtonPosition.LEFT
                ), REQUEST_CODE_BUCK_UP_WALLET)
            } else {
                startActivity(WalletBackupActivity.getCallingIntent(context, getWalletId))
            }
        }

        walletRename.setOnClickListener {
            startActivity(WalletRenameActivity.getCallingIntent(context, getWalletId))
        }

        walletRemove.setOnClickListener {
            if (PinCodeHelper.isAvailable(context)) {
                startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                        context = context,
                        isDisplayFingerprint = true,
                        buttonPosition = NewCheckPinCodeActivity.ButtonPosition.LEFT
                ), REQUEST_CODE_DELETE_WALLET)
            } else {
                startActivity(WalletRemoveActivity.getCallingIntent(context, getWalletId))
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                PinCodeProvider.setPinCode(it.getByteArrayExtra(NewCheckPinCodeActivity.INTENT_PIN_CODE).toString(Charsets.UTF_8))

                when (requestCode) {
                    REQUEST_CODE_BUCK_UP_WALLET -> startActivity(WalletBackupActivity.getCallingIntent(context, getWalletId))
                    REQUEST_CODE_DELETE_WALLET -> {
                        startActivity(WalletRemoveActivity.getCallingIntent(context, getWalletId))
                        finish()
                    }
                }
            }
        }
    }

    private val getWalletId by lazy {
        arguments.getLong(KEY_WALLET_ID)
    }

    private val isMultisig by lazy {
        arguments.getBoolean(KEY_IS_MULTISIG)
    }

    companion object {
        private const val KEY_WALLET_ID = "key_wallet_id"
        private const val KEY_IS_MULTISIG = "key_is_multisig"
        private const val REQUEST_CODE_BUCK_UP_WALLET = 1117
        private const val REQUEST_CODE_DELETE_WALLET = 1118

        fun newInstance(id: Long, isMultisig: Boolean): WalletSettingFragment {
            val fragment = WalletSettingFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.wallet_setting_fragment_title)
            args.putLong(KEY_WALLET_ID, id)
            args.putBoolean(KEY_IS_MULTISIG, isMultisig)
            fragment.arguments = args
            return fragment
        }
    }
}