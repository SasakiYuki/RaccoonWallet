package wacode.yamada.yuki.nempaymentapp.event

import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class WalletInfoEvent {
    class InsertWalletInfo(val walletInfo: WalletInfo) : WalletInfoEvent()
    class UpdateWalletInfo(val walletInfo: WalletInfo) : WalletInfoEvent()
}
