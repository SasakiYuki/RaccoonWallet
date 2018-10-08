package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MyAddressRepository
import wacode.yamada.yuki.nempaymentapp.repository.WalletInfoRepository
import wacode.yamada.yuki.nempaymentapp.repository.WalletRepository
import javax.inject.Inject

class SelectMyProfileAddressAddUseCase @Inject constructor(private val walletRepository: WalletRepository,
                                                           private val myAddressRepository: MyAddressRepository,
                                                           private val walletInfoRepository: WalletInfoRepository) {

    fun findAllWallet() = walletRepository.findAllWallet()

    fun findAllMyAddress() = myAddressRepository.findAllMyAddress()

    fun select(id: Long) = walletInfoRepository.select(id)
}