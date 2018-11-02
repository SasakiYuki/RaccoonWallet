package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class MyWalletInfoActionType {
    class SelectWalletInfo(val walletInfo: WalletInfo) : MyWalletInfoActionType()
    class ReceiveMyAddress(val myAddress: MyAddress) : MyWalletInfoActionType()
    class DeleteMyAddress: MyWalletInfoActionType()
    class Error(val throwable: Throwable) : MyWalletInfoActionType()
}