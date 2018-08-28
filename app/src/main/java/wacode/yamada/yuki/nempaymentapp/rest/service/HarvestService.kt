package wacode.yamada.yuki.nempaymentapp.rest.service


class HarvestService : BaseClientService() {

    fun getHarvestInfo(address: String) = getClient()
            .accountHarvests(address)
            .singleOrError()
}