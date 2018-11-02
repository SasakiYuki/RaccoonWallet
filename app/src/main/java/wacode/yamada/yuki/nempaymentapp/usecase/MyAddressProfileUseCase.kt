package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MyAddressRepository
import wacode.yamada.yuki.nempaymentapp.repository.WalletInfoRepository
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import javax.inject.Inject

class MyAddressProfileUseCase @Inject constructor(private val myAddressRepository: MyAddressRepository,
                                                  private val walletInfoRepository: WalletInfoRepository
) {
    fun insertMyAddress(myAddress: MyAddress) = myAddressRepository.insert(myAddress)

    fun insertWalletInfo(walletInfo: WalletInfo) = walletInfoRepository.insert(walletInfo)
}