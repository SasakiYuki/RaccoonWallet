package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddressDao

class MyAddressRepository {
    private val myAddressDao: MyAddressDao = NemPaymentApplication.database.myAddressDao()

    fun insert(myAddress: MyAddress): Completable {
        return Completable.fromAction {
            myAddressDao.insert(myAddress)
        }
    }

    fun findAllMyAddress(): Observable<MyAddress> {
        return Observable.create {
            val myAddress = myAddressDao.findAll()
            for (item in myAddress) {
                it.onNext(item)
            }
        }
    }

    fun countAllMyAddress(): Single<Int> {
        return Single.create {
            it.onSuccess(myAddressDao.findAll().size)
        }
    }

    fun delete(myAddress: MyAddress): Single<MyAddress> {
        return Single.create {
            myAddressDao.delete(myAddress)
            it.onSuccess(myAddress)
        }
    }
}