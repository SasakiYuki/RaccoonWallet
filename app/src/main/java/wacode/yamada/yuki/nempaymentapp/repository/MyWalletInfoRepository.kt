package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Observable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddressDao
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao

class MyWalletInfoRepository {
    private val myAddressDao: MyAddressDao = NemPaymentApplication.database.myAddressDao()
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun findAllMyAddress(): Observable<MyAddress> {
        return Observable.create {
            val myAddress = myAddressDao.findAll()
            for (item in myAddress) {
                it.onNext(item)
            }
        }
    }

    fun select(id: Long): Single<WalletInfo> {
        return Single.create {
            it.onSuccess(walletInfoDao.select(id))
        }
    }
}