package wacode.yamada.yuki.nempaymentapp.utils

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import io.reactivex.android.schedulers.AndroidSchedulers
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.view.activity.SelectWalletActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SettingActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonAlertType
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonAlertViewModel
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonErrorDialog


object SendStatusUtils {
    fun isAvailable(context: Context): Status {
        return when {
            WalletManager.getSelectedWalletId(context) == 0L -> Status.SELECT_WALLET_ERROR
            !PinCodeHelper.isAvailable(context) -> Status.PIN_CODE_ERROR
            else -> Status.OK
        }
    }

    fun showPinCodeErrorDialog(context: Context, fragmentManager: FragmentManager) {
        val viewModel = RaccoonAlertViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == RaccoonAlertType.BOTTOM_BUTTON) {
                        context.startActivity(SettingActivity.getCallingIntent(context))
                    }
                }

        val dialog = RaccoonErrorDialog.createDialog(viewModel,
                context.getString(R.string.raccoon_error_pin_title),
                context.getString(R.string.raccoon_error_pin_message),
                context.getString(R.string.raccoon_error_pin_button))
        dialog.show(fragmentManager, "")
    }

    fun showWalletErrorDialog(context: Context, fragmentManager: FragmentManager) {
        val viewModel = RaccoonAlertViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == RaccoonAlertType.BOTTOM_BUTTON) {
                        context.startActivity(SelectWalletActivity.createIntent(context))
                    }
                }

        val dialog = RaccoonErrorDialog.createDialog(viewModel,
                context.getString(R.string.raccoon_error_pin_title),
                context.getString(R.string.raccoon_error_wallet_message),
                context.getString(R.string.raccoon_error_wallet_button))
        dialog.show(fragmentManager, "")
    }

    enum class Status {
        OK,
        PIN_CODE_ERROR,
        SELECT_WALLET_ERROR
    }
}