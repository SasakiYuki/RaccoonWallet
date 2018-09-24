package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import wacode.yamada.yuki.nempaymentapp.repository.WalletInfoRepository
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import javax.inject.Inject


class AddressBookUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository, private val walletInfoRepository: WalletInfoRepository) {

    fun getFriendInfo(friendId: Long) = addressBookRepository.queryFriendInfoById(friendId)

    fun queryFriendAddress(friendId: Long) = addressBookRepository.queryFriendAddress(friendId)

    fun queryWalletInfo(walletInfoId: Long) = walletInfoRepository.select(walletInfoId)

    fun insertFriendAddress(friendAddress: FriendAddress) = addressBookRepository.insertOrReplaceFriendAddress(friendAddress)
}