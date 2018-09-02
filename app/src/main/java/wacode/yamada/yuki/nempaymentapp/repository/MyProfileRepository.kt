package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao

class MyProfileRepository {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun create(entity: WalletInfo): Single<WalletInfo> {
        return Single.create {
            walletInfoDao.create(entity)
            it.onSuccess(entity)
        }
    }
}