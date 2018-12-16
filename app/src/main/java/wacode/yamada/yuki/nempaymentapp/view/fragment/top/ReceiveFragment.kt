package wacode.yamada.yuki.nempaymentapp.view.fragment.top

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_receive.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.copyClipBoard
import wacode.yamada.yuki.nempaymentapp.extentions.generateQRCode
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import wacode.yamada.yuki.nempaymentapp.model.PaymentQRInnerEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class ReceiveFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_receive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSelectedWallet(walletId)
        setupRxBus()
    }

    private fun setupRxBus() {
        RxBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        RxBusEvent.SELECT, RxBusEvent.RENAME -> {
                            getSelectedWallet()
                        }
                    }
                }
    }

    private fun setupAddressClick() {
        myAddressTextView.setOnClickListener {
            myAddressTextView.context.showToast(R.string.receive_fragment_address_toast)
            myAddressTextView.text.toString().copyClipBoard(myAddressTextView.context)
        }
    }

    private fun getSelectedWallet(walletId: Long = NOT_EXIST_WALLET_ID) {
        context?.let {
            CoroutineScope(Dispatchers.Main).launch {
                val wallet = async(Dispatchers.IO) {
                    return@async if (walletId == NOT_EXIST_WALLET_ID) {
                        WalletManager.getSelectedWallet(it)
                    } else {
                        WalletManager.getWalletbyId(walletId)
                    }
                }.await()
                wallet?.let {
                    putQRImage(it)
                    setupMyAddress(it)
                    setupAddressClick()
                }
            }
        }
    }

    private fun setupMyAddress(wallet: Wallet) {
        myAddressTextView.text = wallet.displayAddress()
    }

    private fun putQRImage(wallet: Wallet) {
        qrImageView.setImageBitmap(generateQRCode(generateQREntity(wallet)))
    }

    private fun generateQREntity(wallet: Wallet): PaymentQREntity {
        val childEntity = PaymentQRInnerEntity(wallet.address,
                0,
                "",
                wallet.name)
        return PaymentQREntity(data = childEntity)
    }

    private val walletId by lazy {
        arguments?.getLong(ARG_WALLET_ID) ?: 0
    }

    companion object {
        private const val ARG_WALLET_ID = "arg_wallet_id"
        private const val NOT_EXIST_WALLET_ID = -1L

        fun newInstance(walletId: Long = NOT_EXIST_WALLET_ID, titleRes: Int? = null): ReceiveFragment {
            val fragment = ReceiveFragment()
            val args = Bundle()
            args.putLong(ARG_WALLET_ID, walletId)
            titleRes?.let {
                args.putInt(ARG_CONTENTS_NAME_ID, titleRes)
            }
            fragment.arguments = args
            return fragment
        }
    }
}