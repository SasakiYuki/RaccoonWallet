package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import javax.inject.Inject


class AddressBookUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository) {

    fun insertFriendInfo(entity: FriendInfo) = addressBookRepository.insertFriendInfo(entity)

    fun insertFriendIcon(entity: FriendIcon) = addressBookRepository.insertFriendIcon(entity)

    fun getLatestFriendInfo() = addressBookRepository.getLatestFriendInfo()
}