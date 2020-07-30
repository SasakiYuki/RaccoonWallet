package wacode.yamada.yuki.nempaymentapp.model

import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendMessageType
import java.io.Serializable


//data class TransactionAppEntity(
//        val transactionType: TransactionType,
//        val block: Int?,
//        val hash: String?,
//        val date: String?,
//        val fee: String?,
//        val amount: String?,
//        val signer: String?,
//        var senderAddress: String?,
//        val recipientAddress: String?,
//        val isMultisig: Boolean,
//        val message: String?,
//        val timeStamp: Long,
//        val mosaicList: ArrayList<MosaicAppEntity>,
//        val messageType: Int?,
//        val transactionId: Int = 0
//) : Serializable


data class TransactionAppEntity(
        val transactionType: TransactionType,
        val block: Int = 0,
        val hash: String = "",
        val date: String = "",
        val fee: String = "",
        val amount: String = "",
        val signer: String = "",
        var senderAddress: String = "",
        val recipientAddress: String = "",
        val isMultisig: Boolean = false,
        val message: String = "",
        val timeStamp: Long = 0,
        val mosaicList: List<MosaicFullItem> = ArrayList(),
        val messageType: SendMessageType,
        val transactionId: Int = 0
) : Serializable