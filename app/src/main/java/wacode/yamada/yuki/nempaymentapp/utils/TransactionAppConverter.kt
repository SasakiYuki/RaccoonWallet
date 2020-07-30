package wacode.yamada.yuki.nempaymentapp.utils

import com.ryuta46.nemkotlin.enums.MessageType
import com.ryuta46.nemkotlin.model.GeneralTransaction
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import com.ryuta46.nemkotlin.model.UnconfirmedTransactionMetaDataPair
import com.ryuta46.nemkotlin.util.ConvertUtils
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.getNemStartDateTimeLong
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendMessageType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object TransactionAppConverter {
    fun convert(myAddress: String, it: TransactionMetaDataPair) = TransactionAppEntity(
            isInComing(myAddress, it),
            it.meta.height,
            it.meta.hash.data,
            getInstantDate(it.transaction),
            getFee(it.transaction),
            getAmount(it.transaction),
            it.transaction.signer,
            "",
            getRecipientAddress(it.transaction),
            isMultisig(it.transaction),
            getMessage(it.transaction),
            getTimeStamp(it.transaction),
            ArrayList(),
            getMessageType(it.transaction),
            getTransactionId(it)
    )

    fun convert(transactionType: TransactionType, it: TransactionMetaDataPair, senderAddress: String, mosaicItems: List<MosaicFullItem>) = TransactionAppEntity(
            transactionType = transactionType,
            block = it.meta.height,
            hash = it.meta.hash.data,
            date = getDate(it.transaction),
            fee = getFee(it.transaction),
            amount = getAmount(it.transaction),
            signer = it.transaction.signer,
            senderAddress = senderAddress,
            recipientAddress = getRecipientAddress(it.transaction),
            isMultisig = isMultisig(it.transaction),
            message = getMessage(it.transaction),
            timeStamp = getTimeStamp(it.transaction),
            mosaicList = mosaicItems,
            messageType = getMessageType(it.transaction),
            transactionId = getTransactionId(it)
    )

//    fun convert(transactionType: TransactionType, it: UnconfirmedTransactionMetaDataPair) = TransactionAppEntity(
//            transactionType,
//            null,
//            it.meta.data,
//            getDate(it.transaction),
//            getFee(it.transaction),
//            getAmount(it.transaction),
//            it.transaction.signer,
//            "",
//            getRecipientAddress(it.transaction),
//            isMultisig(it.transaction),
//            getMessage(it.transaction),
//            getTimeStamp(it.transaction),
//            getMosaicList(it.transaction),
//            getMessageType(it.transaction)
//    )

    fun convert(transactionType: TransactionType, it: UnconfirmedTransactionMetaDataPair) = TransactionAppEntity(
            transactionType = transactionType,
            hash = it.meta.data,
            date = getDate(it.transaction),
            fee = getFee(it.transaction),
            amount = getAmount(it.transaction),
            signer = it.transaction.signer,
            recipientAddress = getRecipientAddress(it.transaction),
            isMultisig = isMultisig(it.transaction),
            message = getMessage(it.transaction),
            timeStamp = getTimeStamp(it.transaction),
            messageType = getMessageType(it.transaction)
    )

    private fun getTransactionId(transactionMetaDataPair: TransactionMetaDataPair) = transactionMetaDataPair.meta.id

    private fun isInComing(myAddress: String, transactionMetaDataPair: TransactionMetaDataPair): TransactionType {
        return if (myAddress == transactionMetaDataPair.transaction.recipient) TransactionType.INCOMING else TransactionType.OUTGOING
    }

    private fun getAmount(generalTransaction: GeneralTransaction): String {
        return if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.amount?.let {
                it.convertNEMFromMicroToDouble().toString()
            } ?: run { "" }
        } else {
            generalTransaction.amount?.let {
                return it.convertNEMFromMicroToDouble().toString()
            } ?: run { "" }
        }
    }

    private fun getFee(generalTransaction: GeneralTransaction): String {
        return if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.fee.convertNEMFromMicroToDouble().toString()
        } else {
            generalTransaction.fee.convertNEMFromMicroToDouble().toString()
        }
    }

    private fun getRecipientAddress(generalTransaction: GeneralTransaction): String {
        return if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.recipient?.let {
                it.toDisplayAddress()
            } ?: run { "" }
        } else {
            generalTransaction.recipient?.let {
                it.toDisplayAddress()
            } ?: run { "" }
        }
    }

    private fun getDate(generalTransaction: GeneralTransaction): String {
        val nemStartTimeLong = getNemStartDateTimeLong()
        val sdf = SimpleDateFormat("MM/dd,yyyy k:mm;ss")
        return if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            val date = Date(multisigTransaction.timeStamp.toLong() * 1000 + nemStartTimeLong)
            sdf.format(date)
        } else {
            val date = Date(generalTransaction.timeStamp.toLong() * 1000 + nemStartTimeLong)
            sdf.format(date)
        }
    }

    private fun getInstantDate(generalTransaction: GeneralTransaction): String {
        val nemStartTimeLong = getNemStartDateTimeLong()
        val sdf = SimpleDateFormat("MM/dd yyyy")
        return if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            val date = Date(multisigTransaction.timeStamp.toLong() * 1000 + nemStartTimeLong)
            sdf.format(date)
        } else {
            val date = Date(generalTransaction.timeStamp.toLong() * 1000 + nemStartTimeLong)
            sdf.format(date)
        }
    }

    private fun getMessage(generalTransaction: GeneralTransaction): String {
        return generalTransaction.message?.let {
            if (it.type == MessageType.Plain.rawValue) {
                String(ConvertUtils.toByteArray(it.payload), Charsets.UTF_8)
            } else {
                it.payload
            }
        } ?: run { "" }
    }

    private fun getMessageType(generalTransaction: GeneralTransaction): SendMessageType {
        return generalTransaction.message?.let {
            when (it.type) {
                1 -> SendMessageType.NORMAL
                2 -> SendMessageType.CRYPT
                else -> SendMessageType.NONE
            }
        } ?: run {
            SendMessageType.NONE
        }
    }

    private fun getTimeStamp(generalTransaction: GeneralTransaction): Long {
        return if (isMultisig(generalTransaction)) {
            getMultisigTransaction(generalTransaction)!!.otherTrans.timeStamp.toLong()
        } else {
            generalTransaction.timeStamp.toLong()
        }
    }

    private fun isMultisig(generalTransaction: GeneralTransaction): Boolean {
        val item = generalTransaction.asMultisig
        return item != null
    }

    private fun getMultisigTransaction(generalTransaction: GeneralTransaction) = generalTransaction.asMultisig
}