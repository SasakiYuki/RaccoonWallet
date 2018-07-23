package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MosaicRepository
import javax.inject.Inject

class EnterMosaicListUseCase @Inject constructor(
        private val mosaicRepository: MosaicRepository
){
    fun getOwnedMosaics(address: String) =
            mosaicRepository.getOwnedMosaics(address)

    fun getNamespaceMosaics(nameSpace: String) =
            mosaicRepository.getNamespaceMosaics(nameSpace)
}