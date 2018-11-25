package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.view.View
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import java.text.SimpleDateFormat

interface TransactionRowEventHandler {
    fun onTransactionClick(view: View, viewModel: TransactionViewModel)
}

data class TransactionViewModel(val transactionAppEntity: TransactionAppEntity) {

    fun isReceiveNem() = transactionAppEntity.transactionType == TransactionType.INCOMING || transactionAppEntity.transactionType == TransactionType.UNCONFIRMED

    fun amount(): String {
        return if (transactionAppEntity.amount.isNotEmpty()) {
            transactionAppEntity.amount + " XEM"
        } else {
            "取引量不明"
        }
    }

    fun isUnconfirm() = transactionAppEntity.transactionType == TransactionType.UNCONFIRMED

    fun address(): String {
        return if (isReceiveNem()) {
            transactionAppEntity.senderAddress
        } else {
            transactionAppEntity.recipientAddress
        }
    }

    fun multisig() = transactionAppEntity.isMultisig

    fun isMessage() = !transactionAppEntity.message.isEmpty()

    fun isMosaic() = transactionAppEntity.mosaicList.isNotEmpty()

    fun date(): String {
        return if (transactionAppEntity.date.isNotEmpty()) {
            val date = SimpleDateFormat("MM/dd,yyyy k:mm;ss").parse(transactionAppEntity.date)
            return SimpleDateFormat("k:mm").format(date)
        } else {
            "日付不明"
        }
    }
}