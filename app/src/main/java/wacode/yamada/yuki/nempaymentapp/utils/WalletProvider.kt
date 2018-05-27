package wacode.yamada.yuki.nempaymentapp.utils

import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet

object WalletProvider {
    var wallet: Wallet? = null

    fun clearCache() {
        wallet = null
    }
}