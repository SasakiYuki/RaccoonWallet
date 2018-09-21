package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Completable
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddressDao

class MyAddressProfileRepository {
    private val myAddressDao: MyAddressDao = NemPaymentApplication.database.myAddressDao()

    fun insert(myAddress: MyAddress): Completable {
        return Completable.fromAction {
            myAddressDao.insert(myAddress)
        }
    }
}