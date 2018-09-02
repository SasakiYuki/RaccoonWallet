package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddressDao

class MyAddressProfileRepository {
    private val myAddressDao: MyAddressDao = NemPaymentApplication.database.myAddressDao()

    fun create(myAddress: MyAddress): Single<MyAddress> {
        return Single.create {
            myAddressDao.create(myAddress)
            it.onSuccess(myAddress)
        }
    }
}