package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_tutorial_pin_code_end.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.FingerprintHelper
import wacode.yamada.yuki.nempaymentapp.preference.PinCodePreference
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.view.activity.WalletCreatedActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.*

class CompletedPinCodeSettingFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_tutorial_pin_code_end

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton() {
        fingerPrintButton.setOnClickListener {
            if (FingerprintHelper.checkForSave(context)) {
                showFingerprintSettingDialog()
            } else {
                showNotAvailableFingerprintDialog()
            }
        }
        lessonEndButton.setOnClickListener {
            finishSetting()
        }
    }

    private fun showFingerprintSettingDialog() {
        val viewModel = FingerprintSettingViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    when (item) {
                        FingerprintSettingSelectButton.POSITIVE -> PinCodePreference.savePinCodeForFingerprint(context, PinCodeProvider.getPinCode(context)!!.toString(Charsets.UTF_8))
                        FingerprintSettingSelectButton.COMPLETE -> finishSetting()
                    }
                }

        FingerprintSettingDialog.createDialog(viewModel).show(activity.supportFragmentManager, "")
    }

    private fun showNotAvailableFingerprintDialog() {
        val viewModel = RaccoonSelectViewModel(getString(R.string.com_ok), getString(R.string.com_cancel))

        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { item ->
                    if (item.equals(SelectDialogButton.POSITIVE)) {
                        startActivity(Intent().setAction(Settings.ACTION_SECURITY_SETTINGS))
                    }
                }

        RaccoonSelectDialog.createDialog(viewModel,
                getString(R.string.pin_code_setting_end_not_available_title),
                getString(R.string.pin_code_setting_end_not_available_message))
                .show(activity.supportFragmentManager, "")
    }

    private fun finishSetting() {
        startActivity(WalletCreatedActivity.createIntent(context, WalletCreatedType.NEWBIE))
        finish()
    }

    companion object {
        private const val TOOLBAR_STRING_RES = R.string.pin_code_setting_end_fragment_title
        fun newInstance(): CompletedPinCodeSettingFragment {
            val fragment = CompletedPinCodeSettingFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, TOOLBAR_STRING_RES)
            fragment.arguments = bundle
            return fragment
        }
    }
}
