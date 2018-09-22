package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao
import javax.inject.Inject

class WalletInfoRepository {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun select(id: Long): Single<WalletInfo> {
        return Single.create {
            it.onSuccess(walletInfoDao.select(id))
        }
    }
}
