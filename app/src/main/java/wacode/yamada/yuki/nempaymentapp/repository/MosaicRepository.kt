package wacode.yamada.yuki.nempaymentapp.repository

import wacode.yamada.yuki.nempaymentapp.rest.service.MosaicService
import javax.inject.Inject

class MosaicRepository @Inject constructor(private val mosaicService: MosaicService) {
    fun getOwnedMosaics(address: String) =
            mosaicService.getOwnedMosaics(address)

    fun getNamespaceMosaics(nameSpace: String) =
            mosaicService.getNamespaceMosaics(nameSpace)
}

