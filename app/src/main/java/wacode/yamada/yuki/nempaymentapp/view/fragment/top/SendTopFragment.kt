package wacode.yamada.yuki.nempaymentapp.view.fragment.top

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_send_top.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.remove
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.SendActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SettingActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.*
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SendTopFragment : BaseFragment() {
    private val compositeDisposable = CompositeDisposable()
    override fun layoutRes() = R.layout.fragment_send_top

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            async(UI) {
                val wallet = bg { WalletManager.getSelectedWallet(this@SendTopFragment.context) }.await()
                when {
                    wallet == null -> this@SendTopFragment.context.showToast(R.string.send_top_fragment_not_select_wallet)
                    !PinCodeHelper.isAvailable(this@SendTopFragment.context) -> showPinCodeErrorDialog()
                    else -> checkEnterAddressAvailable()
                }
            }
        }
    }

    fun putQRScanItems(paymentQREntity: PaymentQREntity) {
        addressEditText.setText(paymentQREntity.data.addr)
        checkEnterAddressAvailable(paymentQREntity)
    }

    private fun checkEnterAddressAvailable(qrEntity: PaymentQREntity? = null) {
        val address = addressEditText.text.toString().remove("-")
        showProgress()
        compositeDisposable.add(
                NemCommons.getAccountInfo(address)
                        .subscribe({ item ->
                            hideProgress()
                            if (qrEntity != null) {
                                startActivity(SendActivity.createIntentDirectConfirm(context, qrEntity, item.account.publicKey))
                            } else {
                                startActivity(SendActivity.createIntent(context, address, item.account.publicKey))
                            }
                        }, { e ->
                            hideProgress()
                            if (qrEntity != null) {
                                startActivity(SendActivity.createIntentDirectConfirm(context, qrEntity, ""))
                            } else {
                                showNewbieConfirmDialog()
                            }
                        }))
    }

    private fun showNewbieConfirmDialog() {
        val address = addressEditText.text.toString().remove("-")
        val viewModel = RaccoonConfirmViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(SendActivity.createIntent(context, address, ""))
                }
        val title = getString(R.string.send_top_fragment_confirm_title)
        val message = getString(R.string.send_top_fragment_confirm_message)
        val buttonMessage = getString(R.string.com_ok)
        val confirmDialog = RaccoonConfirmDialog.createDialog(
                viewModel,
                title,
                message,
                buttonMessage,
                false)
        confirmDialog.show((context as AppCompatActivity).supportFragmentManager, RaccoonConfirmDialog::class.java.toString())
    }

    private fun showPinCodeErrorDialog() {
        val viewModel = RaccoonAlertViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == RaccoonAlertType.BOTTOM_BUTTON) {
                        startActivity(SettingActivity.getCallingIntent(context))
                    }
                }

        val dialog = RaccoonErrorDialog.createDialog(viewModel,
                getString(R.string.raccoon_error_pin_title),
                getString(R.string.raccoon_error_pin_message),
                getString(R.string.raccoon_error_pin_button))
        dialog.show(activity.supportFragmentManager, "")
    }

    companion object {
        const val VIEW_PAGER_POSITION = 3
        fun newInstance(): SendTopFragment {
            val fragment = SendTopFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.select_wallet_activity_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}