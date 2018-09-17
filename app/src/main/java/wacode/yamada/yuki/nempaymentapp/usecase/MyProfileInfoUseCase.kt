package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.MyAddressRepository
import wacode.yamada.yuki.nempaymentapp.repository.MyProfileRepository
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import javax.inject.Inject

class MyProfileInfoUseCase @Inject constructor(private val myAddressRepository: MyAddressRepository,
                                               private val myProfileRepository: MyProfileRepository) {

    fun countAllMyAddress() = myAddressRepository.countAllMyAddress()

    fun loadMyProfile() = myProfileRepository.loadMyProfile()

    fun updateMyProfile(entity: MyProfile) = myProfileRepository.updateMyProfile(entity)
}