package wacode.yamada.yuki.nempaymentapp.store.type

import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo

sealed class ProfileAddressAddActionType {
    class Insert(val walletInfo: WalletInfo) : ProfileAddressAddActionType()
    class Update(val walletInfo: WalletInfo) : ProfileAddressAddActionType()
    class Error(val throwable: Throwable) : ProfileAddressAddActionType()
}