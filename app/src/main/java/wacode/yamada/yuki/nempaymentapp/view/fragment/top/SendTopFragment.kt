package wacode.yamada.yuki.nempaymentapp.view.fragment.top

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_send_top.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.isNotTextEmptyObservable
import wacode.yamada.yuki.nempaymentapp.extentions.pasteFromClipBoard
import wacode.yamada.yuki.nempaymentapp.extentions.remove
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import wacode.yamada.yuki.nempaymentapp.rest.item.PaymentQrItem
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.view.activity.SendActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.SendType
import wacode.yamada.yuki.nempaymentapp.view.dialog.*
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SendTopFragment : BaseFragment() {
    private val compositeDisposable = CompositeDisposable()
    override fun layoutRes() = R.layout.fragment_send_top

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        button.setOnClickListener { checkEnterAddressAvailable() }
        clipButton.setOnClickListener {
            addressEditText.setText(clipButton.context.pasteFromClipBoard())
        }

        clearButton.setOnClickListener {
            addressEditText.setText("")
        }

        addressEditText.isNotTextEmptyObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        clipButton.visibility = View.GONE
                        clearButton.visibility = View.VISIBLE
                    } else {
                        clipButton.visibility = View.VISIBLE
                        clearButton.visibility = View.GONE
                    }
                }
    }

    fun putQRScanItems(paymentQREntity: PaymentQREntity) {
        addressEditText.setText(paymentQREntity.data.addr)
        checkEnterAddressAvailable(paymentQREntity)
    }

    private fun checkEnterAddressAvailable(qrEntity: PaymentQREntity? = null) {
        val address = addressEditText.text.toString().remove("-")

        if (address.isEmpty()) return

        showProgress()
        compositeDisposable.add(
                NemCommons.getAccountInfo(address)
                        .subscribe({ item ->
                            hideProgress()
                            selectNextScreen(qrEntity, item.account.publicKey)
                        }, { e ->
                            hideProgress()
                            if (qrEntity != null) {
                                selectNextScreen(qrEntity)
                            } else {
                                showNewbieConfirmDialog()
                            }
                        }))
    }

    private fun selectNextScreen(qrEntity: PaymentQREntity? = null, publicKey: String = "") {
        val address = addressEditText.text.toString().remove("-")

        context?.let {
            qrEntity?.let { entity ->
                val paymentItem = PaymentQrItem.createItem(entity)
                if (paymentItem.existAmount()) {
                    if (paymentItem.existMessage()) {
                        showMessageConfirmDialog(address, publicKey, entity)
                    } else {
                        //送金確認画面に遷移
                        startActivity(SendActivity.createIntent(it, address, publicKey, SendType.CONFIRM, entity))
                    }
                } else {
                    showAmountConfirmDialog(address, publicKey, entity)
                }
            } ?: run {
                startActivity(SendActivity.createIntent(it, address, publicKey))
            }
        }
    }

    private fun showNewbieConfirmDialog() {
        val viewModel = RaccoonConfirmViewModel()
        viewModel.clickEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    selectNextScreen()
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

    private fun showAmountConfirmDialog(address: String, publicKey: String, entity: PaymentQREntity) {
        activity?.let { context ->
            val viewModel = RaccoonSelectViewModel(getString(R.string.send_top_fragment_amount_confirm_dialog_positive), getString(R.string.send_top_fragment_amount_confirm_dialog_negative))

            viewModel.clickEvent
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        when (it) {
                            SelectDialogButton.POSITIVE -> {
                                //メッセージを添付するかどうかを選択する画面に遷移
                                startActivity(SendActivity.createIntent(context, address, publicKey, SendType.SELECT_MODE, entity))
                            }
                            SelectDialogButton.NEGATIVE -> {
                                //金額を指定する画面に遷移
                                startActivity(SendActivity.createIntent(context, address, publicKey, SendType.ENTER, entity))
                            }
                        }
                    }

            val title = getString(R.string.send_top_fragment_amount_confirm_title)
            val message = getString(R.string.send_top_fragment_amount_confirm_message)
            val selectDialog = RaccoonSelectDialog.createDialog(viewModel, title, message)
            selectDialog.show(context.supportFragmentManager, RaccoonSelectDialog::class.java.toString())
        }
    }

    private fun showMessageConfirmDialog(address: String, publicKey: String, entity: PaymentQREntity) {
        activity?.let { context ->
            val viewModel = RaccoonSelectViewModel(getString(R.string.send_top_fragment_message_confirm_dialog_positive), getString(R.string.send_top_fragment_message_confirm_dialog_negative))

            viewModel.clickEvent
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        when (it) {
                            SelectDialogButton.POSITIVE -> {
                                //送金確認画面に遷移
                                startActivity(SendActivity.createIntent(context, address, publicKey, SendType.CONFIRM, entity))

                            }
                            SelectDialogButton.NEGATIVE -> {
                                //メッセージの種類を選択する画面に遷移
                                startActivity(SendActivity.createIntent(context, address, publicKey, SendType.SELECT_MESSAGE, entity))
                            }
                        }
                    }

            val title = getString(R.string.send_top_fragment_message_confirm_title)
            val message = getString(R.string.send_top_fragment_message_confirm_message)
            val selectDialog = RaccoonSelectDialog.createDialog(viewModel, title, message)
            selectDialog.show(context.supportFragmentManager, RaccoonSelectDialog::class.java.toString())
        }
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