package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

class MyProfileRepository {
    fun create(entity: WalletInfo): Single<WalletInfo> {
        return Single.create {
            it.onSuccess(entity)
        }
    }
}