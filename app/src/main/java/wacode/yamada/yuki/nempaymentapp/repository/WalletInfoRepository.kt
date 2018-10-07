package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao

class WalletInfoRepository {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun insert(entity: WalletInfo): Single<WalletInfo> {
        return Single.create { emitter ->
            entity.let {
                val id = walletInfoDao.insert(it)
                WalletInfo(id,
                        it.walletName,
                        it.walletAddress,
                        it.isMaster).let {
                    emitter.onSuccess(it)
                }
            }
        }
    }

    fun select(id: Long): Single<WalletInfo> {
        return Single.create {
            it.onSuccess(walletInfoDao.select(id))
        }
    }
}
