package wacode.yamada.yuki.nempaymentapp.rest.service


class AccountService : BaseClientService() {

    fun getAccountInfo(address: String) = getClient()
            .accountGet(address)
            .singleOrError()
}