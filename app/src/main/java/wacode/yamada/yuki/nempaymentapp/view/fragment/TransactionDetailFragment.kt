package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.account.MessageEncryption
import com.ryuta46.nemkotlin.enums.MessageType
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.model.MosaicId
import com.ryuta46.nemkotlin.util.ConvertUtils
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.copyClipBoard
import wacode.yamada.yuki.nempaymentapp.extentions.getColor
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.helper.PinCodeHelper
import wacode.yamada.yuki.nempaymentapp.model.MosaicAppEntity
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.utils.AesCryptographer
import wacode.yamada.yuki.nempaymentapp.utils.PinCodeProvider
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.NewCheckPinCodeActivity


class TransactionDetailFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_transaction_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(transactionAppEntity)
    }

    private fun setupViews(entity: TransactionAppEntity) {
        setupIcons(entity)
        setupAddress(entity)
        setupAmount(entity)
        setupMosaic(entity)
        setupMessage(entity)

        dateText.text = entity.date
        feeText.text = getString(R.string.transaction_detail_fee, entity.fee)
        blockText.text = getString(R.string.transaction_detail_block, entity.block)
        hashText.text = entity.hash
    }

    private fun setupIcons(entity: TransactionAppEntity) {
        val transactionTypeRes = if (entity.transactionType == TransactionType.OUTGOING) {
            R.drawable.icon_transaction_send_red
        } else {
            R.drawable.icon_transaction_receive_green
        }

        transactionTypeIcon.setImageDrawable(activity?.getDrawable(transactionTypeRes))

        if (entity.isMultisig) {
            val multisigIconRes = if (entity.transactionType == TransactionType.OUTGOING) {
                R.drawable.icon_multisignature_red
            } else {
                R.drawable.icon_multisignature_green
            }
            multisgIcon.setImageDrawable(activity?.getDrawable(multisigIconRes))
        }

        unconfirmedView.visibility = if (entity.transactionType == TransactionType.UNCONFIRMED) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setupAddress(entity: TransactionAppEntity) {
        recipientAddressText.text = if (entity.recipientAddress.isNullOrEmpty()) {
            getString(R.string.transaction_detail_non_address)
        } else {
            entity.recipientAddress
        }

        if (entity.senderAddress.isNullOrEmpty()) {
            firstSenderAddressText.text = getString(R.string.transaction_detail_non_address)
            secondSenderAddressText.text = getString(R.string.transaction_detail_non_address)
        } else {
            firstSenderAddressText.text = entity.senderAddress
            secondSenderAddressText.text = entity.senderAddress
        }

        recipientAddressText.setOnClickListener {
            entity.recipientAddress!!.copyClipBoard(recipientAddressText.context)
            recipientAddressText.context.showToast(R.string.com_copied)
        }
        secondSenderAddressText.setOnClickListener {
            entity.senderAddress!!.copyClipBoard(secondSenderAddressText.context)
            secondSenderAddressText.context.showToast(R.string.com_copied)
        }
    }

    private fun setupAmount(entity: TransactionAppEntity) {
        val prefix = if (entity.transactionType == TransactionType.OUTGOING) "-" else "+"
        val prefixColor = if (entity.transactionType == TransactionType.OUTGOING) R.color.colorTransactionRed else R.color.colorTransactionGreen
        if (entity.amount.isNullOrEmpty()) {
            firstAmountText.text = getString(R.string.transaction_detail_non_amount)
            secondAmountText.text = getString(R.string.transaction_detail_second_non_amount)
        } else {
            firstAmountText.text = getString(R.string.transaction_detail_first_amount, entity.amount)
            secondAmountText.text = getString(R.string.transaction_detail_second_amount, entity.amount)
            prefixText.text = prefix
            context?.let {
                prefixText.setTextColor(getColor(it, prefixColor))
            }
        }
    }

    private fun setupMosaic(entity: TransactionAppEntity) {
        if (entity.mosaicList.isEmpty()) {
            mosaicRootView.visibility = View.GONE
            mosaicCheckRootView.visibility = View.GONE
        } else {
            mosaicRootView.visibility = View.VISIBLE
            mosaicCheckRootView.visibility = View.VISIBLE
            mosaicText.text = convertMosaics(entity.mosaicList)
        }
    }

    private fun convertMosaics(mosaics: ArrayList<MosaicAppEntity>): String {
        return buildString {
            append(getString(R.string.transaction_detail_mosaic_count, mosaics.size))

            for (mosaic in mosaics) {
                append("\n")

                val name = getString(R.string.transaction_detail_mosaic_name, mosaic.mosaicIdAppEntity.name)
                val amount = if (mosaic.mosaicIdAppEntity.fullName == MosaicId("nem", "xem").fullName) {
                    getString(R.string.transaction_detail_mosaic_amount, mosaic.quantity.convertNEMFromMicroToDouble().toString())

                } else {
                    mosaic.quantity.toString()
                }

                append(name)
                append("   ")
                append(amount)
            }
        }
    }

    private fun setupMessage(entity: TransactionAppEntity) {
        if (entity.message.isNullOrEmpty()) {
            messageText.text = getString(R.string.transaction_detail_non_message)
            massageCheckRootView.visibility = View.GONE
        } else {
            massageCheckRootView.visibility = View.VISIBLE

            if (entity.messageType == MessageType.Encrypted.rawValue) {
                messageText.visibility = View.GONE
                decryptButton.visibility = View.VISIBLE
            } else {
                messageText.visibility = View.VISIBLE
                decryptButton.visibility = View.GONE
                messageText.text = entity.message
            }
        }

        decryptButton.setOnClickListener {
            if (PinCodeHelper.isAvailable(decryptButton.context)) {
                startActivityForResult(NewCheckPinCodeActivity.getCallingIntent(
                        context = decryptButton.context,
                        isDisplayFingerprint = true,
                        messageRes = R.string.transaction_detail_pin_code_title,
                        buttonPosition = NewCheckPinCodeActivity.ButtonPosition.RIGHT
                ), REQUEST_CODE_PIN_CODE)
            } else {
                PinCodeProvider.getPinCode(decryptButton.context)?.let {
                    showEncryptMessage(it, entity)
                }
            }
        }
    }

    private fun showEncryptMessage(pin: ByteArray, entity: TransactionAppEntity) {
        showProgress()
        context?.let {
            showEncryptMessage(it, pin, entity)
        }
    }

    private fun showEncryptMessage(context: Context, pin: ByteArray, entity: TransactionAppEntity) {
        async(UI) {
            bg { WalletManager.getSelectedWallet(context) }
                    .await()
                    ?.let {
                        try {
                            val rawKey = AesCryptographer.decrypt(it.encryptedSecretKey, it.salt, pin.toString(Charsets.UTF_8)).toString(Charsets.UTF_8)
                            val account = AccountGenerator.fromSeed(ConvertUtils.swapByteArray(ConvertUtils.toByteArray(rawKey)), Version.Main)
                            val decryptedBytes = MessageEncryption.decrypt(account, ConvertUtils.toByteArray(entity.signer!!), ConvertUtils.toByteArray(entity.message!!))
                            messageText.text = String(decryptedBytes, Charsets.UTF_8)
                            decryptButton.visibility = View.GONE
                            messageText.visibility = View.VISIBLE
                        } catch (e: Exception) {
                            context.showToast(R.string.transaction_detail_decrypt_error_message)
                        }
                    }
                    ?: run { context.showToast(R.string.transaction_detail_decrypt_error_message) }
            hideProgress()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PIN_CODE -> {
                    if (data != null && data.hasExtra(NewCheckPinCodeActivity.INTENT_PIN_CODE)) {
                        showEncryptMessage(data.getByteArrayExtra(NewCheckPinCodeActivity.INTENT_PIN_CODE), transactionAppEntity)
                    }
                }
            }
        }
    }

    private val transactionAppEntity by lazy {
        arguments?.getSerializable(KEY_TRANSACTION_DETAIL) as TransactionAppEntity
    }

    companion object {
        const val KEY_TRANSACTION_DETAIL = "key_transaction_detail"
        private const val REQUEST_CODE_PIN_CODE = 1020

        fun newInstance(entity: TransactionAppEntity): TransactionDetailFragment {
            val fragment = TransactionDetailFragment()
            val args = Bundle().apply {
                putSerializable(KEY_TRANSACTION_DETAIL, entity)
                putInt(ARG_CONTENTS_NAME_ID, R.string.transaction_detail_title)
            }

            fragment.arguments = args
            return fragment
        }
    }
}