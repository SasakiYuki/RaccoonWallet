package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import javax.inject.Inject


class CreateAddressBookUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository) {

    fun insertFriendInfo(entity: FriendInfo) = addressBookRepository.insertOrReplaceFriendInfo(entity)

    fun insertFriendWallet(entity: FriendAddress) = addressBookRepository.insertOrReplaceFriendAddress(entity)

    fun queryLatestFriendInfo() = addressBookRepository.queryLatestFriendInfo()
}