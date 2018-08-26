package wacode.yamada.yuki.nempaymentapp.rest.service


class TransactionService : BaseClientService() {

    fun geAllTransaction(address: String) = getClient()
            .accountTransfersAll(address)
            .singleOrError()
}