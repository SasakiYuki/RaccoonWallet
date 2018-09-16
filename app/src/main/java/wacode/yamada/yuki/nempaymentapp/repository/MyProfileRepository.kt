package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfileDao

class MyProfileRepository {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()
    private val myProfileDao: MyProfileDao = NemPaymentApplication.database.myProfileDao()

    fun createWalletInfo(entity: WalletInfo): Single<WalletInfo> {
        return Single.create { emitter ->
            entity.let {
                WalletInfo(walletInfoDao.create(it),
                        it.walletName,
                        it.walletAddress,
                        it.isMaster).let {
                    emitter.onSuccess(it)
                }
            }
        }
    }

    fun loadMyProfile(): Single<MyProfile> {
        return Single.create { emitter ->
            myProfileDao.findAll().getOrNull(0)?.let {
                emitter.onSuccess(it)
            }
        }
    }

    fun createMyProfile(entity: MyProfile): Single<Unit> {
        return Single.create { emitter ->
            myProfileDao.create(entity)
            emitter.onSuccess(Unit)
        }
    }
}