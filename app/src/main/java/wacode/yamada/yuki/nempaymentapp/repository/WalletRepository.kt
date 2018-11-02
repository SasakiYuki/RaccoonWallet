package wacode.yamada.yuki.nempaymentapp.repository

import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.room.wallet.WalletDao

class WalletRepository {
    private val walletDao: WalletDao = NemPaymentApplication.database.walletDao()
    fun findAllWallet(): Single<List<Wallet>> {
        return Single.create {
            it.onSuccess(walletDao.findAll())
        }
    }
}