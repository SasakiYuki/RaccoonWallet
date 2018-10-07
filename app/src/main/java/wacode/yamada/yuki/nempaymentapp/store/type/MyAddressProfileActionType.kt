package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class MyAddressProfileActionType {
    class InsertMyAddress : MyAddressProfileActionType()
    class InsertWalletInfo(val walletInfo: WalletInfo) : MyAddressProfileActionType()
}