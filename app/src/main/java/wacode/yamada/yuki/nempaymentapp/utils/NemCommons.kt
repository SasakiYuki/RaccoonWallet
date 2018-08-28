package wacode.yamada.yuki.nempaymentapp.utils

import com.ryuta46.nemkotlin.account.Account
import com.ryuta46.nemkotlin.account.AccountGenerator
import com.ryuta46.nemkotlin.account.MessageEncryption
import com.ryuta46.nemkotlin.client.RxNemApiClient
import com.ryuta46.nemkotlin.enums.MessageType
import com.ryuta46.nemkotlin.enums.Version
import com.ryuta46.nemkotlin.model.NemAnnounceResult
import com.ryuta46.nemkotlin.transaction.MosaicAttachment
import com.ryuta46.nemkotlin.transaction.TransactionHelper
import com.ryuta46.nemkotlin.util.ConvertUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.rest.ApiManager
import wacode.yamada.yuki.nempaymentapp.rest.item.TransactionMosaicItem
import wacode.yamada.yuki.nempaymentapp.view.activity.SendMessageType

object NemCommons {
    const val DEFAULT_NEM_NAMESPACE = "nem"
    const val DEFAULT_NEM_NAME = "xem"

    fun getClient() = RxNemApiClient(ApiManager.getBaseUrl())

    @Deprecated(message = "AccoutService->getAccountInfo()")
    fun getAccountInfo(address: String) =
            getClient().accountGet(address)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getAccountTransfersIncoming(address: String) =
            getClient().accountTransfersIncoming(address)
                    .subscribeOn(Schedulers.newThread())

    fun getAccountTransfersOutgoing(address: String) =
            getClient().accountTransfersOutgoing(address)
                    .subscribeOn(Schedulers.newThread())

    fun getAccountUnconfirmedTransactions(address: String) =
            getClient().accountUnconfirmedTransactions(address)
                    .subscribeOn(Schedulers.newThread())

    fun getAccountInfoFromPublicKey(publicKey: String) =
            getClient().accountGetFromPublicKey(publicKey)
                    .subscribeOn(Schedulers.newThread())

    fun getAccountMosaicOwned(publicKey: String) =
            getClient().accountMosaicOwned(publicKey)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getNamespaceMosaics(nameSpace: String) =
            getClient().namespaceMosaicDefinitionPage(nameSpace)

    fun createTransaction(account: Account, receiverAddress: String, mosaics: ArrayList<TransactionMosaicItem>, message: String, messageType: SendMessageType, senderPublicKey: String): Observable<NemAnnounceResult>? {
        val byteArrayMessage = createMessage(account, senderPublicKey, messageType, message)
        val messageTypeAnnounce = when (messageType) {
            SendMessageType.NONE,
            SendMessageType.NORMAL ->
                MessageType.Plain
            SendMessageType.CRYPT ->
                MessageType.Encrypted
        }
        if (isOnlyXemTransaction(mosaics)) {
            return getClient().transactionAnnounce(TransactionHelper.createXemTransferTransaction(account, receiverAddress = receiverAddress, microNem = mosaics[0].quantity, message = byteArrayMessage, messageType = messageTypeAnnounce))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else if (isIncludeXemTransaction(mosaics)) {
            var xemItem: TransactionMosaicItem? = null
            val otherMosaics = ArrayList<TransactionMosaicItem>()
            for (item in mosaics) {
                if (isNemXEM(item.nameSpace, item.mosaicName)) {
                    xemItem = item
                } else {
                    otherMosaics.add(item)
                }
            }
            val xemTransaction = getClient().transactionAnnounce(TransactionHelper.createXemTransferTransaction(account, receiverAddress = receiverAddress, microNem = xemItem!!.quantity, message = byteArrayMessage, messageType = messageTypeAnnounce))
            val list = ArrayList<MosaicAttachment>()
            otherMosaics.mapTo(list) { MosaicAttachment(it.nameSpace, it.mosaicName, it.quantity, it.supply, it.divisibility) }
            val transaction = TransactionHelper.createMosaicTransferTransaction(account,
                    receiverAddress,
                    list)
            val mosaicsTransaction = getClient().transactionAnnounce(transaction)
            return xemTransaction.flatMap { mosaicsTransaction }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
        }

        val list = ArrayList<MosaicAttachment>()
        mosaics.mapTo(list) { MosaicAttachment(it.nameSpace, it.mosaicName, it.quantity, it.supply, it.divisibility) }
        val transaction = TransactionHelper.createMosaicTransferTransaction(account,
                receiverAddress,
                list,
                message = byteArrayMessage,
                messageType = messageTypeAnnounce)
        return getClient().transactionAnnounce(transaction)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createMessage(account: Account, senderPublicKey: String, messageType: SendMessageType, message: String): ByteArray {
        val senderPublicKeyArray = ConvertUtils.toByteArray(senderPublicKey)
        return when (messageType) {
            SendMessageType.NONE,
            SendMessageType.NORMAL ->
                message.toByteArray(Charsets.UTF_8)
            SendMessageType.CRYPT ->
                MessageEncryption.encrypt(account, senderPublicKeyArray, message.toByteArray(Charsets.UTF_8))
        }
    }

    private fun isOnlyXemTransaction(mosaics: ArrayList<TransactionMosaicItem>) = mosaics.size > 0 && mosaics.size == 1 && isNemXEM(mosaics[0].nameSpace, mosaics[0].mosaicName)

    fun isNemXEM(nameSpace: String, mosaicName: String) = nameSpace == DEFAULT_NEM_NAMESPACE && mosaicName == DEFAULT_NEM_NAME

    fun isIncludeXemTransaction(mosaics: List<TransactionMosaicItem>) = mosaics.any { isNemXEM(it.nameSpace, it.mosaicName) }


    fun getQuantityAndSupply(nameSpace: String, mosaicName: String) =
            getClient().namespaceMosaicDefinitionFromName(nameSpace, mosaicName)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

    fun createAccount(privateKey: String) = AccountGenerator.fromSeed(ConvertUtils.swapByteArray(ConvertUtils.toByteArray(privateKey)), Version.Main)


}