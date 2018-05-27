package wacode.yamada.yuki.nempaymentapp.utils

import com.ryuta46.nemkotlin.transaction.MosaicAttachment
import com.ryuta46.nemkotlin.transaction.TransactionHelper
import com.ryuta46.nemkotlin.transaction.TransactionHelper.Companion.calculateMessageTransferFee
import com.ryuta46.nemkotlin.transaction.TransactionHelper.Companion.calculateXemTransferFee
import wacode.yamada.yuki.nempaymentapp.extentions.convertMicroNEM
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import wacode.yamada.yuki.nempaymentapp.rest.item.TransactionMosaicItem
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons.isNemXEM

object FeeCalculator {
    private const val minimumTransferFee = 50_000L

    private fun calculatorXemFee(microNem: Long, message: ByteArray): Long {
        return Math.max(minimumTransferFee, calculateXemTransferFee(microNem) + calculateMessageTransferFee(message))
    }

    private fun calculatorMosaicFee(mosaics: List<TransactionMosaicItem>, message: ByteArray): Long {
        val list = ArrayList<MosaicAttachment>()
        mosaics.mapTo(list) { MosaicAttachment(it.nameSpace, it.mosaicName, it.quantity, it.supply, it.divisibility) }
        return when {
            list.isEmpty() -> Math.max(minimumTransferFee, calculateXemTransferFee(0) + calculateMessageTransferFee(message))
            else -> {
                var mosaicTransferFeeTotal = 0L
                list.forEach {
                    mosaicTransferFeeTotal += TransactionHelper.calculateMosaicTransferFee(it)
                }
                Math.max(minimumTransferFee, mosaicTransferFeeTotal + calculateMessageTransferFee(message))
            }
        }
    }

    private fun calculatorFee(mosaics: List<TransactionMosaicItem>, message: ByteArray): Long {
        when {
            isOnlyXemTransaction(mosaics) -> return calculatorXemFee(mosaics[0].quantity, message)
            NemCommons.isIncludeXemTransaction(mosaics) -> {
                var xemItem: TransactionMosaicItem? = null
                val otherMosaics = ArrayList<TransactionMosaicItem>()
                for (item in mosaics) {
                    if (isNemXEM(item.nameSpace, item.mosaicName)) {
                        xemItem = item
                    } else {
                        otherMosaics.add(item)
                    }
                }
                var fee: Long = 0
                xemItem?.let {
                    fee += calculatorXemFee(it.quantity, message)
                }
                fee += calculatorMosaicFee(otherMosaics, message)
                return fee
            }
            else -> return calculatorMosaicFee(mosaics, message)
        }
    }

    fun calculatorFeeSendMosaicItem(mosaics: List<SendMosaicItem>, message: ByteArray): Long {
        val list = ArrayList<TransactionMosaicItem>()
        for (item in mosaics) {
            list.add(TransactionMosaicItem(
                    item.nameSpace(),
                    item.mosaicName(),
                    0L,
                    0,
                    item.amount.convertMicroNEM()
            ))
        }
        return calculatorFee(list, message)
    }

    private fun isOnlyXemTransaction(mosaics: List<TransactionMosaicItem>) = mosaics.size > 0 && mosaics.size == 1 && NemCommons.isNemXEM(mosaics[0].nameSpace, mosaics[0].mosaicName)
}