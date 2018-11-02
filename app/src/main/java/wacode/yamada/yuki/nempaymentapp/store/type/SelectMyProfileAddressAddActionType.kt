package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet

sealed class SelectMyProfileAddressAddActionType {
    class ReceiveAllWallet(val list: List<Wallet>) : SelectMyProfileAddressAddActionType()
    class ReceiveMyAddress(val myAddress: MyAddress) : SelectMyProfileAddressAddActionType()
    class ReceiveWalletInfo(val walletInfo: WalletInfo) : SelectMyProfileAddressAddActionType()
}