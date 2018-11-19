package wacode.yamada.yuki.nempaymentapp.rest.service


class AccountService : BaseClientService() {

    fun getAccountInfo(address: String) = getClient()
            .accountGet(address)
            .singleOrError()

    fun grtAccountGetFromPublicKey(publicKey: String) = getClient()
            .accountGetFromPublicKey(publicKey = publicKey)
            .singleOrError()
}