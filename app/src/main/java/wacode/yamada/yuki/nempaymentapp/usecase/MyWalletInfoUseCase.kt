package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MyAddressRepository
import wacode.yamada.yuki.nempaymentapp.repository.WalletInfoRepository
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import javax.inject.Inject

class MyWalletInfoUseCase @Inject constructor(private val myAddressRepository: MyAddressRepository,
                                              private val walletInfoRepository: WalletInfoRepository) {
    fun findAllMyAddress() = myAddressRepository.findAllMyAddress()

    fun select(id: Long) = walletInfoRepository.select(id)

    fun deleteMyAddress(myAddress: MyAddress) = myAddressRepository.delete(myAddress)
}