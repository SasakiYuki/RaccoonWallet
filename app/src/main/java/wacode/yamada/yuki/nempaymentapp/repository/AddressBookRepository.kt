package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfoSortType
import javax.inject.Inject


class AddressBookRepository @Inject constructor() {
    private val addressBookDao: AddressBookDao = NemPaymentApplication.database.addressBookDao()
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun insertOrReplaceFriendInfo(entity: FriendInfo): Completable {
        return Completable.fromAction {
            addressBookDao.insertOrReplace(entity)
        }
    }

    fun insertOrReplaceFriendAddress(entity: FriendAddress): Completable {
        return Completable.fromAction {
            addressBookDao.insertOrReplace(entity)
        }
    }

    fun insertOrReplaceWalletInfo(entity: WalletInfo): Single<WalletInfo> {
        return Single.create { emitter ->
            entity.let {
                WalletInfo(walletInfoDao.insert(it),
                        it.walletName,
                        it.walletAddress,
                        it.isMaster).let {
                    emitter.onSuccess(it)
                }
            }
        }
    }

    fun queryFriendInfoById(friendId: Long) = addressBookDao.queryFriendInfo(friendId)

    fun queryLatestFriendInfo() = addressBookDao.queryLatestFriendInfo()

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

    fun queryFriendAddress(friendId: Long) = addressBookDao.queryFriendAddress(friendId)

    fun removeFriendAddress(walletInfoId: Long): Completable {
        return Completable.fromAction {
            addressBookDao.removeFriendAddress(walletInfoId)
        }
    }
}