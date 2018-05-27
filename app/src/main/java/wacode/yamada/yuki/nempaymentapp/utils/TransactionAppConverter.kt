package wacode.yamada.yuki.nempaymentapp.utils

import com.ryuta46.nemkotlin.enums.MessageType
import com.ryuta46.nemkotlin.model.AccountMetaDataPair
import com.ryuta46.nemkotlin.model.GeneralTransaction
import com.ryuta46.nemkotlin.model.TransactionMetaDataPair
import com.ryuta46.nemkotlin.model.UnconfirmedTransactionMetaDataPair
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.extentions.convertNEMFromMicroToDouble
import wacode.yamada.yuki.nempaymentapp.extentions.getNemStartDateTimeLong
import wacode.yamada.yuki.nempaymentapp.extentions.toDisplayAddress
import wacode.yamada.yuki.nempaymentapp.model.MosaicAppEntity
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
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
            getMosaicList(it.transaction),
            getMessageType(it.transaction)
    )

    fun convert(transactionType: TransactionType, it: TransactionMetaDataPair) = TransactionAppEntity(
            transactionType,
            it.meta.height,
            it.meta.hash.data,
            getDate(it.transaction),
            getFee(it.transaction),
            getAmount(it.transaction),
            it.transaction.signer,
            "",
            getRecipientAddress(it.transaction),
            isMultisig(it.transaction),
            getMessage(it.transaction),
            getTimeStamp(it.transaction),
            getMosaicList(it.transaction),
            getMessageType(it.transaction)
    )

    fun convert(transactionType: TransactionType, it: UnconfirmedTransactionMetaDataPair) = TransactionAppEntity(
            transactionType,
            null,
            it.meta.data,
            getDate(it.transaction),
            getFee(it.transaction),
            getAmount(it.transaction),
            it.transaction.signer,
            "",
            getRecipientAddress(it.transaction),
            isMultisig(it.transaction),
            getMessage(it.transaction),
            getTimeStamp(it.transaction),
            getMosaicList(it.transaction),
            getMessageType(it.transaction)
    )

    fun convert(it: TransactionMetaDataPair, transactionType: TransactionType, accountMetaDataPair: AccountMetaDataPair) = TransactionAppEntity(
            transactionType,
            it.meta.height,
            it.meta.hash.data,
            getDate(it.transaction),
            getFee(it.transaction),
            getAmount(it.transaction),
            it.transaction.signer,
            getSenderAddress(it.transaction, accountMetaDataPair),
            getRecipientAddress(it.transaction),
            isMultisig(it.transaction),
            getMessage(it.transaction),
            getTimeStamp(it.transaction),
            getMosaicList(it.transaction),
            getMessageType(it.transaction)
    )

    fun convert(it: UnconfirmedTransactionMetaDataPair, transactionType: TransactionType, accountMetaDataPair: AccountMetaDataPair) = TransactionAppEntity(
            transactionType,
            null,
            it.meta.data,
            getDate(it.transaction),
            getFee(it.transaction),
            getAmount(it.transaction),
            it.transaction.signer,
            getSenderAddress(it.transaction, accountMetaDataPair),
            getRecipientAddress(it.transaction),
            isMultisig(it.transaction),
            getMessage(it.transaction),
            getTimeStamp(it.transaction),
            getMosaicList(it.transaction),
            getMessageType(it.transaction)
    )

    private fun isInComing(myAddress: String, transactionMetaDataPair: TransactionMetaDataPair): TransactionType {
        return if (myAddress == transactionMetaDataPair.transaction.recipient) TransactionType.INCOMING else TransactionType.OUTGOING
    }

    private fun getAmount(generalTransaction: GeneralTransaction): String? {
        if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.amount?.let {
                return it.convertNEMFromMicroToDouble().toString()
            }
            return null
        } else {
            generalTransaction.amount?.let {
                return it.convertNEMFromMicroToDouble().toString()
            }
            return null
        }
    }

    private fun getFee(generalTransaction: GeneralTransaction): String? {
        if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.fee?.let {
                return it.convertNEMFromMicroToDouble().toString()
            }
            return null
        } else {
            generalTransaction.fee?.let {
                return it.convertNEMFromMicroToDouble().toString()
            }
            return null
        }
    }

    private fun getRecipientAddress(generalTransaction: GeneralTransaction): String? {
        if (isMultisig(generalTransaction)) {
            val multisigTransaction = getMultisigTransaction(generalTransaction)!!.otherTrans
            multisigTransaction.recipient?.let {
                return it.toDisplayAddress()
            }
            return null
        } else {
            generalTransaction.recipient?.let {
                return it.toDisplayAddress()
            }
            return null
        }
    }

    private fun getSenderAddress(generalTransaction: GeneralTransaction, accountMetaDataPair: AccountMetaDataPair): String? {
        if (isMultisig(generalTransaction)) {
            if (accountMetaDataPair.meta.cosignatoryOf.isNotEmpty()) {
                accountMetaDataPair.meta.cosignatoryOf[0].address?.let {
                    return it.toDisplayAddress()
                }
            } else {
                return null
            }
        } else {
            return accountMetaDataPair.account.address.toDisplayAddress()
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

    private fun getMessage(generalTransaction: GeneralTransaction): String? {
        val message = generalTransaction.message
        return if (message != null) {
            if (message.type == MessageType.Plain.rawValue) {
                String(ConvertUtils.toByteArray(message.payload), Charsets.UTF_8)
            } else {
                message.payload
            }
        } else {
            ""
        }
    }

    private fun getMessageType(generalTransaction: GeneralTransaction): Int? {
        return generalTransaction.message?.let {
            it.type
        } ?: run {
            null
        }
    }

    private fun getTimeStamp(generalTransaction: GeneralTransaction): Long {
        if (isMultisig(generalTransaction)) {
            return getMultisigTransaction(generalTransaction)!!.otherTrans.timeStamp.toLong()
        } else {
            return generalTransaction.timeStamp.toLong()
        }
    }

    private fun getMosaicList(generalTransaction: GeneralTransaction): ArrayList<MosaicAppEntity> {
        return generalTransaction.mosaics?.let {
            Observable.fromIterable(it)
                    .map { mosaic ->
                        MosaicAppEntity(mosaicId = mosaic.mosaicId,
                                quantity = mosaic.quantity)
                    }
                    .toList()
                    .blockingGet() as ArrayList<MosaicAppEntity>
        } ?: kotlin.run {
            return ArrayList()
        }
    }

    private fun isMultisig(generalTransaction: GeneralTransaction): Boolean {
        val item = generalTransaction.asMultisig
        return item != null
    }

    private fun getMultisigTransaction(generalTransaction: GeneralTransaction) = generalTransaction.asMultisig
}