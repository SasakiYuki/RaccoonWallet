package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfoSortType
import javax.inject.Inject


class AddressBookRepository @Inject constructor() {
    private val addressBookDao: AddressBookDao

    init {
        addressBookDao = NemPaymentApplication.database.addressBookDao()
    }

    fun insertFriendInfo(entity: FriendInfo): Completable {
        return Completable.fromAction {
            addressBookDao.insertOrReplace(entity)
        }
    }

    fun queryFriendInfoById(friendId: Long) = addressBookDao.queryFriendInfo(friendId)

    fun queryFriendInfo(queryName: String, sortType: FriendInfoSortType): Single<List<FriendInfo>> {
        val patternMathText = "%$queryName%"

        //todo sortTypeの追加
        return when (sortType) {
            FriendInfoSortType.WELL_SEND -> addressBookDao.queryFriendInfoOrderByName(patternMathText)
            else -> addressBookDao.queryFriendInfoOrderByName(patternMathText)
        }
    }

    fun queryFriendInfo(queryName: String, isTwitterAuth: Boolean, sortType: FriendInfoSortType): Single<List<FriendInfo>> {
        val patternMathText = "%$queryName%"

        //todo sortTypeの追加
        return when (sortType) {
            FriendInfoSortType.WELL_SEND -> addressBookDao.queryFriendInfoOrderByName(patternMathText, isTwitterAuth)
            else -> addressBookDao.queryFriendInfoOrderByName(patternMathText, isTwitterAuth)
        }
    }
}