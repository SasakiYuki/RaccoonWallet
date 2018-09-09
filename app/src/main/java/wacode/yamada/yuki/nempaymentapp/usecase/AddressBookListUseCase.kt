package wacode.yamada.yuki.nempaymentapp.usecase

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.repository.AddressBookRepository
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.view.custom_view.BackLayerSearchView
import javax.inject.Inject


class AddressBookListUseCase @Inject constructor(private val addressBookRepository: AddressBookRepository) {
    fun getFriendIconPath(friendId: Long) = addressBookRepository
            .getFriendIconById(friendId)
            .map { it.iconPath }
            .onErrorReturnItem("")

    fun findFriendInfoByNameAndSearchType(word: String, type: BackLayerSearchView.SearchType): Single<List<FriendInfo>> {
        return when (type) {
            BackLayerSearchView.SearchType.TWITTER -> addressBookRepository.findFriendInfoByNameAndTwitterAuth(word, true)
            BackLayerSearchView.SearchType.LOCAL -> addressBookRepository.findFriendInfoByNameAndTwitterAuth(word, false)
            else -> addressBookRepository.findPatterMatchFriendInfoByName(word)
        }
    }
}