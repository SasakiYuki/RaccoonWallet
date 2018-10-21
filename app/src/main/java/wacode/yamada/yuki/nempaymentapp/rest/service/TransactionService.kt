package wacode.yamada.yuki.nempaymentapp.rest.service


class TransactionService : BaseClientService() {

    fun geAllTransaction(address: String, id: Int) = getClient()
            .accountTransfersAll(address = address, id = id)
            .singleOrError()

    fun getUnconfirmedTransactions(address: String) = getClient()
            .accountUnconfirmedTransactions(address = address)
            .singleOrError()
}