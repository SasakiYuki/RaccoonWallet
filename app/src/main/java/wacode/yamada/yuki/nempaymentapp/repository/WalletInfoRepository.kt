package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao

class WalletInfoRepository {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun select(id: Long): Single<WalletInfo> {
        return Single.create {
            it.onSuccess(walletInfoDao.select(id))
        }
    }

    fun remove(walletInfo: WalletInfo): Completable {
        return Completable.fromAction {
            walletInfoDao.delete(walletInfo)
        }
    }
}
