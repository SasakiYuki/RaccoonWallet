package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import javax.inject.Inject


class AddressBookRepository @Inject constructor() {
    private val addressBookDao: AddressBookDao

    init {
        addressBookDao = NemPaymentApplication.database.addressBookDao()
    }

    fun insertFriendInfo(entity: FriendInfo): Completable {
        return Completable.fromAction {
            addressBookDao.insert(entity)
        }
    }

    fun insertFriendIcon(entity: FriendIcon): Completable {
        return Completable.fromAction {
            addressBookDao.insert(entity)
        }
    }

    fun getLatestFriendInfo() = addressBookDao.getLatestFriendInfo()

    fun getFriendInfoById(friendId: Long) = addressBookDao.getSingleFriendInfo(friendId)

    fun getFriendIconById(friendId: Long) = addressBookDao.getSingleFriendIcon(friendId)

    fun getAllFriendInfo() = addressBookDao.findAllFriendInfo()

    fun findPatterMatchFriendInfoByName(word: String): Single<List<FriendInfo>> {
        val patternMathText = "%$word%"
        return addressBookDao.findPatterMatchFriendInfoByName(patternMathText)
    }

    fun findFriendInfoByNameAndTwitterAuth(word: String, isTwitterAuth: Boolean): Single<List<FriendInfo>> {
        val patternMathText = "%$word%"
        return addressBookDao.findFriendInfoByNameAndTwitterAuth(patternMathText, isTwitterAuth)
    }
}