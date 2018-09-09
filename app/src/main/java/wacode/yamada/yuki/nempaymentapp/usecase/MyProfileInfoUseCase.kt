package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MyAddressRepository
import javax.inject.Inject

class MyProfileInfoUseCase @Inject constructor(private val myAddressRepository: MyAddressRepository){

    fun countAllMyAddress() = myAddressRepository.countAllMyAddress()
}