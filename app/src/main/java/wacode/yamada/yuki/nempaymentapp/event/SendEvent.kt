package wacode.yamada.yuki.nempaymentapp.event

import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class SendEvent {
    class SendFromWalletInfo(val walletInfo: WalletInfo) : SendEvent()
}