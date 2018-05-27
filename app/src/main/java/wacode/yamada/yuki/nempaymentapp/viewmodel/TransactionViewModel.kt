package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.view.View
import wacode.yamada.yuki.nempaymentapp.model.MosaicAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import java.text.SimpleDateFormat

interface TransactionRowEventHandler {
    fun onTransactionClick(view: View, viewModel: TransactionViewModel)
}

interface TransactionRowLongEventHandler {
    fun onTransactionLongClick(view: View, viewModel: TransactionViewModel): Boolean
}

data class TransactionViewModel(val transactionAppEntity: TransactionAppEntity) {

    fun isReceiveNem() = transactionAppEntity.transactionType.equals(TransactionType.INCOMING) || transactionAppEntity.transactionType.equals(TransactionType.UNCONFIRMED)

    fun amount(): String {
        transactionAppEntity.amount?.let {
            return it + " XEM"
        }
        return "取引量不明"
    }

    fun isUnconfirm() = transactionAppEntity.transactionType == TransactionType.UNCONFIRMED

    fun address(): String {
        if (isReceiveNem()) {
            transactionAppEntity.senderAddress?.let {
                return it
            }
        } else {
            transactionAppEntity.recipientAddress?.let {
                return it
            }
        }
        return "アドレス不明"
    }

    fun message(): String {
        transactionAppEntity.message?.let {
            return it
        }
        return "no message"
    }

    fun multisig(): Boolean {
        return transactionAppEntity.isMultisig
    }

    fun isMessage(): Boolean {
        transactionAppEntity.message?.let {
            return true
        }
        return false
    }

    fun isMosaic() = !transactionAppEntity.mosaicList.isEmpty()

    fun date(): String {
        transactionAppEntity.date?.let {
            val date = SimpleDateFormat("MM/dd,yyyy k:mm;ss").parse(it)
            return SimpleDateFormat("k:mm").format(date)
        }
        return "日付不明"
    }
}