package wacode.yamada.yuki.nempaymentapp.rest.service

class MosaicService : BaseClientService() {

    fun getOwnedMosaics(address: String) =
            getClient().accountMosaicOwned(address)

    fun getNamespaceMosaics(nameSpace:String) =
            getClient().namespaceMosaicDefinitionPage(nameSpace)
}