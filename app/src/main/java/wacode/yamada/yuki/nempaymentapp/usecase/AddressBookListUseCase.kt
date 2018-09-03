package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import javax.inject.Inject


class AddressBookListUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository) {
    fun getAllFriendInfo() = addressBookRepository.getAllFriendInfo()

    fun getFriendIconPath(friendId: Long) = addressBookRepository
            .getFriendIconById(friendId)
            .map { it.iconPath }
            .onErrorReturnItem("")
}