package wacode.yamada.yuki.nempaymentapp.model

import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import java.io.Serializable


data class TransactionAppEntity(
        val transactionType: TransactionType,
        val block: Int?,
        val hash: String?,
        val date: String?,
        val fee: String?,
        val amount: String?,
        val signer: String?,
        var senderAddress: String?,
        val recipientAddress: String?,
        val isMultisig: Boolean,
        val message: String?,
        val timeStamp: Long,
        val mosaicList: ArrayList<MosaicAppEntity>,
        val messageType: Int?
) : Serializable