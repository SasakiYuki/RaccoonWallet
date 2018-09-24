package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import javax.inject.Inject


class CreateAddressBookUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository) {

    fun insertFriendInfo(entity: FriendInfo) = addressBookRepository.insertOrReplaceFriendInfo(entity)

    fun insertFriendAddress(entity: FriendAddress) = addressBookRepository.insertOrReplaceFriendAddress(entity)

    fun insertWalletInfo(entity: WalletInfo) = addressBookRepository.insertOrReplaceWalletInfo(entity)

    fun queryLatestFriendInfo() = addressBookRepository.queryLatestFriendInfo()
}